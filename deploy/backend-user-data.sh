#!/bin/bash
# ==============================================================================
# User Data - Backend Videojuegos (Spring Boot / Java 17) en EC2
# Amazon Linux 2023 - Subred PRIVADA (con salida a Internet por NAT Gateway)
#
# El Nginx del frontend envía /api/* a  http://10.0.3.50:3000/api/*
# => Lanza esta instancia en la subred privada 10.0.3.0/24 con IP privada 10.0.3.50
#
# SOLO HAY QUE EDITAR EL BLOQUE "CONFIGURACIÓN" (credenciales de la BD).
# Log de ejecución: /var/log/videojuegos-user-data.log
# ==============================================================================

# ============================ CONFIGURACIÓN ===================================
DB_HOST="CAMBIAR-endpoint-de-RDS-o-IP-privada-de-MySQL"   # ej. videojuegos-db.xxxx.us-east-1.rds.amazonaws.com
DB_PORT="3306"
DB_NAME="videojuegos_db"        # db_script.sql crea esta base; no cambiar salvo que edites el script
DB_USER="admin"                 # usuario maestro de RDS (o el creado en la instancia MySQL)
DB_PASSWORD="CAMBIAR-password"
DB_SSL_MODE="PREFERRED"         # PREFERRED funciona con RDS y con MySQL en EC2

# Cargar el script SQL en la BD si aún no existen las tablas (seguro de re-ejecutar:
# si la tabla 'usuarios' ya existe NO se toca nada).
INIT_DB="true"
SQL_FILE="db_script.sql"        # o "docker/01_init.sql" (misma estructura + imágenes reales, ~8 MB)

APP_PORT="3000"
APP_JWT_SECRET="4a0efa73ee27ab4b2ecaa6955ea9c0d33a72281eb78c4addc6a83238e53c6114"

REPO_URL="https://github.com/21-Juan-Hdz/videojuegos-backend.git"
REPO_BRANCH="main"
# ==============================================================================

set -euo pipefail
exec > >(tee -a /var/log/videojuegos-user-data.log) 2>&1
echo "=== Inicio user-data backend: $(date) ==="
export HOME=/root

APP_DIR=/opt/videojuegos
SRC_DIR=$APP_DIR/src
ENV_FILE=/etc/videojuegos/backend.env

# 1. Swap de 1 GB (Maven + Spring Boot en t2/t3.micro se quedan cortos de RAM)
if [ ! -f /swapfile ]; then
  dd if=/dev/zero of=/swapfile bs=1M count=1024
  chmod 600 /swapfile
  mkswap /swapfile
  swapon /swapfile
  echo '/swapfile swap swap defaults 0 0' >> /etc/fstab
fi

# 2. Paquetes: git, Java 17 (Corretto), Maven y cliente MySQL
dnf install -y git java-17-amazon-corretto-devel
dnf install -y maven-amazon-corretto17 || dnf install -y maven
dnf install -y mysql8.4 || dnf install -y mariadb105

JAVA_HOME=$(ls -d /usr/lib/jvm/java-17-amazon-corretto* | head -n 1)
export JAVA_HOME
export PATH="$JAVA_HOME/bin:$PATH"
java -version

# 3. Código fuente
mkdir -p "$APP_DIR"
rm -rf "$SRC_DIR"
git clone --depth 1 -b "$REPO_BRANCH" "$REPO_URL" "$SRC_DIR"

# 4. Compilar el JAR
cd "$SRC_DIR"
mvn -B -q -DskipTests package
cp target/*.jar "$APP_DIR/app.jar"

# 5. Variables de entorno de la aplicación (solo root puede leerlas)
mkdir -p /etc/videojuegos
cat > "$ENV_FILE" <<EOF
SERVER_PORT=$APP_PORT
DB_HOST=$DB_HOST
DB_PORT=$DB_PORT
DB_NAME=$DB_NAME
DB_USER=$DB_USER
DB_PASSWORD=$DB_PASSWORD
DB_SSL_MODE=$DB_SSL_MODE
APP_JWT_SECRET=$APP_JWT_SECRET
EOF
chmod 600 "$ENV_FILE"

# 6. Inicializar la base de datos (opcional)
if [ "$INIT_DB" = "true" ]; then
  export MYSQL_PWD="$DB_PASSWORD"
  MYSQL_OPTS=(-h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" --max_allowed_packet=64M)
  if mysql --version | grep -qi mariadb; then MYSQL_OPTS+=(--ssl); fi

  echo "Esperando a que la BD responda en $DB_HOST:$DB_PORT ..."
  for i in $(seq 1 60); do
    if mysql "${MYSQL_OPTS[@]}" -e "SELECT 1" >/dev/null 2>&1; then break; fi
    sleep 10
  done

  TABLES=$(mysql "${MYSQL_OPTS[@]}" -N -e \
    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='$DB_NAME' AND table_name='usuarios';" || echo "ERR")
  if [ "$TABLES" = "0" ]; then
    echo "Cargando $SQL_FILE en la BD..."
    mysql "${MYSQL_OPTS[@]}" < "$SRC_DIR/$SQL_FILE"
    echo "Script SQL cargado."
  elif [ "$TABLES" = "ERR" ]; then
    echo "AVISO: no se pudo conectar a la BD. Revisa DB_HOST/credenciales/Security Groups."
  else
    echo "La BD ya tiene tablas; no se ejecuta el script."
  fi
  unset MYSQL_PWD
fi

# 7. Servicio systemd (arranca solo y se reinicia si falla)
id videojuegos >/dev/null 2>&1 || useradd --system --no-create-home --shell /sbin/nologin videojuegos
chown -R videojuegos:videojuegos "$APP_DIR"

cat > /etc/systemd/system/videojuegos-backend.service <<EOF
[Unit]
Description=Videojuegos Backend (Spring Boot)
After=network-online.target
Wants=network-online.target

[Service]
User=videojuegos
EnvironmentFile=$ENV_FILE
ExecStart=$JAVA_HOME/bin/java -Xms128m -Xmx512m -Duser.timezone=UTC -jar $APP_DIR/app.jar
Restart=always
RestartSec=5
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl enable --now videojuegos-backend

# 8. Verificación
for i in $(seq 1 30); do
  if curl -fs "http://localhost:$APP_PORT/actuator/health/liveness" >/dev/null; then
    echo "Backend arriba en el puerto $APP_PORT"
    curl -s "http://localhost:$APP_PORT/api/health/db"; echo
    break
  fi
  sleep 5
done

echo "=== Fin user-data backend: $(date) ==="
