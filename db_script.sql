DROP DATABASE IF EXISTS videojuegos_db;
CREATE DATABASE IF NOT EXISTS videojuegos_db;
USE videojuegos_db;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS devoluciones;
DROP TABLE IF EXISTS factura;
DROP TABLE IF EXISTS detalle_venta;
DROP TABLE IF EXISTS venta;
DROP TABLE IF EXISTS carrito;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS usuarios;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE usuarios (
  id_usuario BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  telefono VARCHAR(30),
  fecha_ingreso DATE,
  rol VARCHAR(50),
  estado VARCHAR(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE producto (
  id_videojuegos BIGINT PRIMARY KEY AUTO_INCREMENT,
  titulo VARCHAR(200) NOT NULL,
  descripcion TEXT,
  precio DECIMAL(12,2) NOT NULL,
  stock INT NOT NULL,
  plataforma VARCHAR(20),
  genero VARCHAR(60),
  fecha_lanzamiento DATE,
  estado VARCHAR(30),
  imagen_url LONGTEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE carrito (
  id_carrito BIGINT PRIMARY KEY AUTO_INCREMENT,
  id_usuario BIGINT NOT NULL,
  id_videojuego BIGINT NOT NULL,
  fecha_creacion DATETIME,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(12,2),
  CONSTRAINT fk_carrito_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
  CONSTRAINT fk_carrito_producto FOREIGN KEY (id_videojuego) REFERENCES producto(id_videojuegos)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE venta (
  id_venta BIGINT PRIMARY KEY AUTO_INCREMENT,
  id_usuario BIGINT NOT NULL,
  fecha_venta DATETIME,
  total DECIMAL(12,2) NOT NULL,
  metodo_pago VARCHAR(50),
  estado VARCHAR(30),
  CONSTRAINT fk_venta_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE detalle_venta (
  id_detalle_venta BIGINT PRIMARY KEY AUTO_INCREMENT,
  id_venta BIGINT NOT NULL,
  id_videojuego BIGINT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(12,2) NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_detven_venta FOREIGN KEY (id_venta) REFERENCES venta(id_venta),
  CONSTRAINT fk_detven_producto FOREIGN KEY (id_videojuego) REFERENCES producto(id_videojuegos)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE factura (
  id_factura BIGINT PRIMARY KEY AUTO_INCREMENT,
  id_venta BIGINT NOT NULL,
  numero_factura VARCHAR(50) NOT NULL UNIQUE,
  fecha_emision DATETIME,
  total DECIMAL(12,2) NOT NULL,
  impuestos DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_factura_venta FOREIGN KEY (id_venta) REFERENCES venta(id_venta)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE devoluciones (
  id_devolucion BIGINT PRIMARY KEY AUTO_INCREMENT,
  id_usuario BIGINT NOT NULL,
  id_factura BIGINT NOT NULL,
  fecha_devolucion DATETIME,
  descripcion TEXT,
  estado VARCHAR(30),
  observacion TEXT,
  CONSTRAINT fk_devol_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
  CONSTRAINT fk_devol_factura FOREIGN KEY (id_factura) REFERENCES factura(id_factura)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE favoritos (
  id_favorito BIGINT PRIMARY KEY AUTO_INCREMENT,
  id_usuario BIGINT NOT NULL,
  id_videojuego BIGINT NOT NULL,
  CONSTRAINT fk_favo_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
  CONSTRAINT fk_favo_factura FOREIGN KEY (id_usuario) REFERENCES producto(id_videojuegos)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- USUARIOS
-- =========================
INSERT INTO usuarios(nombre, apellido, email, password, telefono, fecha_ingreso, rol, estado)
VALUES 
('Admin', 'System', 'admin@maestria.com', '$2a$10$kY1xT2o0AqkB0A5pCENg0uH4sXnhpXxkM8hY0q2V0w5f3k2gYtT8K', '7755220066', CURDATE(), 'ADMIN', 'ACTIVO'),
('Juan', 'Hernandez', 'juan@test.com', '$2a$10$kY1xT2o0AqkB0A5pCENg0uH4sXnhpXxkM8hY0q2V0w5f3k2gYtT8K', '5512345678', CURDATE(), 'CLIENTE', 'ACTIVO'),
('Maria', 'Lopez', 'maria@test.com', '$2a$10$kY1xT2o0AqkB0A5pCENg0uH4sXnhpXxkM8hY0q2V0w5f3k2gYtT8K', '5598765432', CURDATE(), 'CLIENTE', 'ACTIVO');

-- =========================
-- PRODUCTOS (Base64 simulado)
-- =========================
INSERT INTO producto(
  titulo, descripcion, precio, stock, plataforma, genero,
  fecha_lanzamiento, estado, imagen_url
)
VALUES
(
  'The Legend of Zelda: Breath of the Wild',
  'Aventura de mundo abierto',
  1299.00, 20, 'Switch', 'Aventura',
  '2017-03-03', 'DISPONIBLE',
  'iVBORw0KGgoAAAANSUhEUgAAAAUAFAKEZELDA=='
),
(
  'God of War Ragnarok',
  'Acción y mitología nórdica',
  1499.00, 15, 'PS5', 'Acción',
  '2022-11-09', 'DISPONIBLE',
  'R0lGODlhAQABAIAAAAUEBAKEGOWAR=='
),
(
  'Halo Infinite',
  'Shooter en primera persona',
  999.00, 30, 'Xbox', 'FPS',
  '2021-12-08', 'DISPONIBLE',
  'UklGRiIAAABXRUJQVlA4IBYAAAAwAQCdASoBAAEAAUAmJQBOgA=='
),
(
  'FIFA 24',
  'Simulador de fútbol',
  1199.00, 25, 'Multiplataforma', 'Deportes',
  '2023-09-29', 'DISPONIBLE',
  'Qk2eAAAAAAAAADYAAAAoAAAAAQABAAEAGwAAAAAAAP///w=='
);

-- =========================
-- CARRITO
-- =========================
INSERT INTO carrito(id_usuario, id_videojuego, fecha_creacion, cantidad, precio_unitario)
VALUES
(2, 1, NOW(), 1, 1299.00),
(2, 3, NOW(), 2, 999.00),
(3, 2, NOW(), 1, 1499.00);

-- =========================
-- VENTA
-- =========================
INSERT INTO venta(id_usuario, fecha_venta, total, metodo_pago, estado)
VALUES
(2, NOW(), 3297.00, 'TARJETA', 'COMPLETADA'),
(3, NOW(), 1499.00, 'PAYPAL', 'COMPLETADA');

-- =========================
-- DETALLE VENTA
-- =========================
INSERT INTO detalle_venta(id_venta, id_videojuego, cantidad, precio_unitario, subtotal)
VALUES
(1, 1, 1, 1299.00, 1299.00),
(1, 3, 2, 999.00, 1998.00),
(2, 2, 1, 1499.00, 1499.00);

-- =========================
-- FACTURA
-- =========================
INSERT INTO factura(id_venta, numero_factura, fecha_emision, total, impuestos)
VALUES
(1, 'FAC-0001', NOW(), 3297.00, 527.52),
(2, 'FAC-0002', NOW(), 1499.00, 239.84);

-- =========================
-- DEVOLUCIONES
-- =========================
INSERT INTO devoluciones(id_usuario, id_factura, fecha_devolucion, descripcion, estado, observacion)
VALUES
(2, 1, NOW(), 'Producto defectuoso', 'APROBADA', 'Se autoriza devolución completa');
