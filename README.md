# Videojuegos Backend (Spring Boot 3.5 / Java 17 / MySQL)

API REST de la tienda de videojuegos. Todas las rutas empiezan con `/api` y corre en el **puerto 3000**,
que es lo que espera el Nginx del frontend
([gamestore, rama SisVirt-Act2](https://github.com/Cristofer3097/gamestore/tree/SisVirt-Act2)):

```
Navegador ──► ALB ──► EC2 Frontend (Nginx)  ──/api/*──►  EC2 Backend 10.0.3.50:3000  ──►  MySQL (RDS o EC2)
               (subred pública)                        (subred privada)                 (subred privada)
```

## Configuración: solo variables de entorno

| Variable         | Default          | Descripción                                             |
|------------------|------------------|---------------------------------------------------------|
| `DB_HOST`        | `localhost`      | Endpoint de RDS o IP privada de la instancia MySQL       |
| `DB_PORT`        | `3306`           |                                                         |
| `DB_NAME`        | `videojuegos_db` | Base que crea `db_script.sql`                           |
| `DB_USER`        | `root`           | Usuario de la BD                                        |
| `DB_PASSWORD`    | `12345`          | Contraseña de la BD                                     |
| `DB_SSL_MODE`    | `PREFERRED`      | `PREFERRED` / `REQUIRED` / `DISABLED`                   |
| `SERVER_PORT`    | `3000`           | Puerto HTTP                                             |
| `APP_JWT_SECRET` | (valor de dev)   | Secreto para firmar los JWT                             |

Usuarios semilla del script SQL (contraseña `12345`): `admin@maestria.com`, `juan@test.com`, `maria@test.com`.

## Endpoints útiles

- `GET /api/health/db`: estado de la conexión a MySQL (muestra URL y usuario, nunca la contraseña)
- `GET /actuator/health/liveness`: responde 200 si la app está viva (para health checks)
- `GET /api/producto`: catálogo
- Swagger: `/docs/`

---

## Levantar con Docker (conecta a Amazon RDS)

Los datos de conexión están en [`docker-compose.yml`](docker-compose.yml) (`DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`).

```bash
docker compose up --build -d
# http://localhost:3000/api/health/db   -> "status": "UP"
# http://localhost:3000/api/producto
docker compose logs -f api     # ver logs
docker compose down            # detener
```

La máquina donde corre Docker debe poder llegar a la RDS por el puerto 3306
(misma VPC y Security Group de la RDS permitiendo 3306 desde esa máquina).

Sin Docker (con un MySQL local ya cargado con `db_script.sql`):

```bash
# PowerShell
$env:DB_HOST="localhost"; $env:DB_USER="root"; $env:DB_PASSWORD="tu_password"
mvn spring-boot:run
```

---

## Despliegue en AWS (todo en una misma cuenta)

### 1. Red (VPC)
- Subredes públicas (2 AZ): ALB + frontend. Internet Gateway.
- Subredes privadas (2 AZ): backend y BD. **NAT Gateway** en la tabla de rutas privada (el backend
  necesita Internet para descargar Java/Maven y clonar este repo).
- El backend debe ir en la subred privada **`10.0.3.0/24`**, porque el Nginx del front apunta a `10.0.3.50:3000`.

### 2. Security Groups
| SG       | Entrada                                   |
|----------|-------------------------------------------|
| `sg-alb` | 80 desde `0.0.0.0/0`                      |
| `sg-front` | 80 desde `sg-alb`                       |
| `sg-back`  | **3000 desde `sg-front`**               |
| `sg-db`    | **3306 desde `sg-back`**                |

### 3. Base de datos (elige una opción)

**Opción A: Amazon RDS MySQL (recomendada; Multi-AZ = alta disponibilidad)**
- Motor MySQL 8.x, *DB subnet group* con las 2 subredes privadas, **sin acceso público**, SG `sg-db`.
- Anota el **endpoint**, el usuario maestro (ej. `admin`) y la contraseña.
- No hace falta crear la base: el backend carga `db_script.sql` automáticamente la primera vez.

**Opción B: MySQL en una EC2 (tercera instancia)**
- Amazon Linux 2023 en subred privada, SG `sg-db`.
- *User data*: el contenido de [`deploy/db-mysql-ec2-user-data.sh`](deploy/db-mysql-ec2-user-data.sh)
  (cambia `DB_PASSWORD` antes).
- Anota su IP privada; esa es `DB_HOST`. El usuario es `videojuegos`.

### 4. Backend (EC2)
1. Lanza una EC2 **Amazon Linux 2023** (t3.small recomendado; t2/t3.micro funciona porque el script crea swap).
2. Red: VPC del proyecto, subred privada `10.0.3.0/24`, sin IP pública, SG `sg-back`.
   En *Network settings → Advanced → Primary IP* pon **`10.0.3.50`**.
3. *Advanced details → User data*: pega [`deploy/backend-user-data.sh`](deploy/backend-user-data.sh) y
   **edita solo el bloque CONFIGURACIÓN**:
   ```bash
   DB_HOST="videojuegos-db.xxxxxx.us-east-1.rds.amazonaws.com"   # o IP privada de la EC2 MySQL
   DB_USER="admin"
   DB_PASSWORD="tu_password"
   ```
4. Espera unos 5 a 8 minutos (compila con Maven). El script:
   instala Java 17 y Maven, clona el repo, compila, carga `db_script.sql` en la BD si está vacía y
   deja la app como servicio `systemd` (`videojuegos-backend`) en el puerto 3000.

### 5. Frontend
Se despliega con el `deploy/user-data.sh` del repo del front (Launch Template + Auto Scaling Group + ALB).
No requiere cambios mientras el backend esté en `10.0.3.50:3000`.

### 6. Verificación
Desde el navegador (DNS del ALB):
- `http://<dns-del-alb>/api/health/db` → `"status": "UP"`
- `http://<dns-del-alb>/api/producto` → lista de juegos

Desde la EC2 del backend (Session Manager o bastión):
```bash
sudo tail -n 50 /var/log/videojuegos-user-data.log    # salida del user-data
sudo journalctl -u videojuegos-backend -f              # logs de la app
curl localhost:3000/api/health/db
```

### Cambiar credenciales después del despliegue
```bash
sudo vi /etc/videojuegos/backend.env        # DB_HOST, DB_USER, DB_PASSWORD...
sudo systemctl restart videojuegos-backend
```

### Problemas comunes
| Síntoma | Causa probable |
|---------|----------------|
| `/api/health/db` → `Communications link failure` | `sg-db` no permite 3306 desde `sg-back`, o `DB_HOST` incorrecto |
| `Access denied for user` | Usuario o contraseña incorrectos |
| `Unknown database 'videojuegos_db'` | No se cargó el script: revisa el log del user-data o cárgalo a mano: `mysql -h $DB_HOST -u admin -p < /opt/videojuegos/src/db_script.sql` |
| El front muestra 502 en `/api` | El backend no está en `10.0.3.50`, no escucha en el 3000, o `sg-back` no permite tráfico desde `sg-front` |
| El user-data no clona/instala nada | La subred privada no tiene ruta `0.0.0.0/0` hacia el NAT Gateway |
