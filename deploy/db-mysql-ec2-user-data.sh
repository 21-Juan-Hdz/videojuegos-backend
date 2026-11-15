#!/bin/bash
# ==============================================================================
# User Data - Base de datos MySQL en una EC2 (ALTERNATIVA a Amazon RDS)
# Amazon Linux 2023 - Subred PRIVADA (con salida a Internet por NAT Gateway)
#
# Solo crea el servidor, la base y el usuario. Las TABLAS y DATOS los carga
# automáticamente el backend (backend-user-data.sh con INIT_DB="true").
#
# En el backend usa:  DB_HOST=<IP privada de esta instancia>  DB_USER / DB_PASSWORD = los de abajo
# Log: /var/log/videojuegos-db-user-data.log
# ==============================================================================

# ============================ CONFIGURACIÓN ===================================
DB_NAME="videojuegos_db"
DB_USER="videojuegos"
DB_PASSWORD="CAMBIAR-password"
ALLOWED_HOST="10.0.%"     # desde dónde se permite conectar (rango de la VPC)
# ==============================================================================

set -euo pipefail
exec > >(tee -a /var/log/videojuegos-db-user-data.log) 2>&1
echo "=== Inicio user-data BD: $(date) ==="

# MySQL 8.4 si está disponible en el repositorio; si no, MariaDB (compatible)
if dnf install -y mysql8.4-server; then
  SERVICE=mysqld
  CONF=/etc/my.cnf.d/zz-videojuegos.cnf
else
  dnf install -y mariadb105-server
  SERVICE=mariadb
  CONF=/etc/my.cnf.d/zz-videojuegos.cnf
fi

cat > "$CONF" <<EOF
[mysqld]
bind-address=0.0.0.0
max_allowed_packet=64M
character-set-server=utf8mb4
EOF

systemctl enable --now "$SERVICE"

# Base de datos y usuario (root local se autentica por socket)
mysql -u root <<EOF
CREATE DATABASE IF NOT EXISTS \`$DB_NAME\` CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS '$DB_USER'@'$ALLOWED_HOST' IDENTIFIED BY '$DB_PASSWORD';
GRANT ALL PRIVILEGES ON \`$DB_NAME\`.* TO '$DB_USER'@'$ALLOWED_HOST';
FLUSH PRIVILEGES;
EOF

echo "=== MySQL listo: base '$DB_NAME', usuario '$DB_USER'@'$ALLOWED_HOST' ==="
