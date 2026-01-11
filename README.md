Videojuegos (Spring Boot 3.5.2 / Java 17) - MySQL ONLY

Config:
- Se usa SOLO application.yml (application.properties fue removido para evitar conflictos).
- Arranca aunque MySQL esté abajo: repositorios JPA se inicializan en modo deferred + lazy-init.
- Cuando MySQL no esté disponible, los endpoints que usen DB fallarán al momento de invocarlos.
- Para verificar conexión:
  - GET /api/health/db (detallado)
  - GET /actuator/health

Run:
mvn spring-boot:run

Swagger:
http://localhost:8080/docs/swagger-ui.html
## Docker

Levantar API + MySQL:
docker compose up --build

Swagger:
http://localhost:8080/docs/swagger-ui.html

Health DB:
http://localhost:8080/api/health/db

Actuator:
http://localhost:8080/actuator/health

Notas:
- MySQL ejecuta el script ./docker/01_init.sql la primera vez (cuando el volumen mysql_data está vacío).
- Si ya levantaste antes y cambiaste el script, borra el volumen para re-ejecutar:
  docker compose down -v

Nota Docker:
- Se fija el dialecto Hibernate (MySQLDialect) para que la app arranque incluso si MySQL tarda o si se deshabilita el acceso a metadata.
