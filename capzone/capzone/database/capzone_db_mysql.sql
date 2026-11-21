-- =====================================================================
-- CapZone - Base de datos (MySQL 8)
-- Ejecutar COMPLETO en MySQL Workbench (rayo amarillo o Ctrl+Shift+Enter).
-- =====================================================================
DROP DATABASE IF EXISTS capzone_db;
CREATE DATABASE capzone_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE capzone_db;

-- ---------------------------------------------------------------------
-- Tablas
-- ---------------------------------------------------------------------
CREATE TABLE usuarios (
    id_usuario      INT PRIMARY KEY AUTO_INCREMENT,
    nombre_completo VARCHAR(100) NOT NULL,
    correo          VARCHAR(50)  NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,          -- hash SHA-256 (64 caracteres)
    telefono        VARCHAR(15)  NOT NULL,
    fecha_registro  DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categorias (
    id_categoria     INT PRIMARY KEY AUTO_INCREMENT,
    nombre_categoria VARCHAR(50) NOT NULL UNIQUE,
    descripcion      VARCHAR(500) NULL
);

CREATE TABLE productos (
    id_producto     INT PRIMARY KEY AUTO_INCREMENT,
    id_categoria    INT NOT NULL,
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion     VARCHAR(500) NULL,
    precio          DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    inventario      INT NOT NULL CHECK (inventario >= 0),
    url_imagen      VARCHAR(255) NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

CREATE TABLE pedidos (
    id_pedido     INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario    INT NOT NULL,
    fecha_pedido  DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado_pedido VARCHAR(20) NOT NULL DEFAULT 'Pendiente',
    total         DECIMAL(10,2) NOT NULL,
    CONSTRAINT ck_pedidos_estado CHECK (estado_pedido IN ('Pendiente','Confirmado','Enviado','Entregado')),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE detalle_pedidos (
    id_detalle      INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido       INT NOT NULL,
    id_producto     INT NOT NULL,
    cantidad        INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido)   REFERENCES pedidos(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

CREATE TABLE mensajes_contacto (
    id_contacto INT PRIMARY KEY AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    correo      VARCHAR(50)  NOT NULL,
    asunto      VARCHAR(100) NULL,
    mensaje     VARCHAR(1000) NOT NULL,
    fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
    leido       BOOLEAN NOT NULL DEFAULT 0
);

-- ---------------------------------------------------------------------
-- Datos de prueba
-- Todos los usuarios tienen la contraseña 123456 (guardada como hash SHA-256,
-- igual que lo hace PasswordHasher.java).
-- ---------------------------------------------------------------------
INSERT INTO categorias (nombre_categoria, descripcion) VALUES
('Snapback', 'Gorras de visera plana con broche regulable trasero clásico.'),
('Dad Hat',  'Gorras de visera curva, desestructuradas, de estilo retro y cómodo.'),
('Trucker',  'Gorras con malla trasera transpirable, ideales para climas cálidos y estilo urbano.'),
('Beanie',   'Gorros de lana o materiales elásticos para protección en climas fríos.');

INSERT INTO productos (id_categoria, nombre_producto, descripcion, precio, inventario, url_imagen) VALUES
(1, 'Gorra Classic Snapback Black', 'Gorra negra de visera plana de alta densidad, 100% algodón.', 89900.00, 25, 'images/products/snapback-black.jpg'),
(1, 'Gorra Snapback Urban Red', 'Estilo urbano en tonalidad rojo carmesí con relieve frontal.', 95000.00, 15, 'images/products/snapback-red.jpg'),
(1, 'Gorra Snapback Street Camo', 'Estampado de camuflaje militar clásico, broche reforzado.', 99900.00, 10, 'images/products/snapback-camo.jpg'),
(2, 'Gorra Dad Hat Vintage Blue', 'Gorra pre-lavada azul mezclilla con ajuste metálico correa.', 75000.00, 30, 'images/products/dadhat-blue.jpg'),
(2, 'Gorra Dad Hat Pastel Pink', 'Diseño minimalista rosa pastel, tela suave transpirable.', 75000.00, 20, 'images/products/dadhat-pink.jpg'),
(3, 'Gorra Trucker Retro Summer', 'Frente blanco acolchado, visera naranja y malla premium.', 68000.00, 40, 'images/products/trucker-summer.jpg'),
(3, 'Gorra Trucker Dark All-Black', 'Malla y frente negro completo, ideal para personalizaciones.', 65000.00, 50, 'images/products/trucker-black.jpg'),
(3, 'Gorra Trucker Outdoor Green', 'Diseño verde oliva con malla beige, parche de aventura.', 72000.00, 18, 'images/products/trucker-green.jpg'),
(4, 'Gorro Beanie Winter Grey', 'Tejido de punto grueso color gris jaspeado, talla única.', 45000.00, 15, 'images/products/beanie-grey.jpg'),
(4, 'Gorro Beanie Docker Mustard', 'Estilo corto por encima de la oreja en tono mostaza de tendencia.', 48000.00, 12, 'images/products/beanie-mustard.jpg');

INSERT INTO usuarios (nombre_completo, correo, password, telefono) VALUES
('Juan Pérez Gómez',      'juan.perez@email.com',   '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '3157778899'),
('María Camila Restrepo', 'maria.camila@email.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '3004445566'),
('Carlos Mario Giraldo',  'carlos.mario@email.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '3102221100'),
('Sergio Castaño',        'sergio@capzone.com',     '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '3000000000');

INSERT INTO pedidos (id_usuario, fecha_pedido, estado_pedido, total) VALUES
(1, '2026-06-15 10:30:00', 'Confirmado', 254800.00),
(2, '2026-06-20 16:45:00', 'Enviado',    140000.00),
(1, '2026-06-25 09:15:00', 'Pendiente',   45000.00);

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario) VALUES
(1, 1, 2, 89900.00),
(1, 4, 1, 75000.00),
(2, 6, 1, 68000.00),
(2, 8, 1, 72000.00),
(3, 9, 1, 45000.00);

INSERT INTO mensajes_contacto (nombre, correo, asunto, mensaje) VALUES
('Andrés Mendoza', 'andres.m@email.com', 'Compra al por mayor', 'Hola, me interesa comprar al por mayor de la gorra Trucker Dark All-Black.'),
('Diana Tobón',    'diana.t@email.com',  'Envíos',              '¿Hacen envíos a municipios fuera de Medellín?');

-- ---------------------------------------------------------------------
-- Procedimientos almacenados
-- (La app Java registra los pedidos con su propia transacción para poder
--  comprar varios productos a la vez; este SP queda como versión de 1 producto.)
-- ---------------------------------------------------------------------
DELIMITER $$

CREATE PROCEDURE sp_ReporteComprasUsuario(
    IN p_id_usuario   INT,
    IN p_fecha_inicio DATETIME,
    IN p_fecha_fin    DATETIME
)
BEGIN
    SELECT
        u.nombre_completo,
        u.correo,
        p.id_pedido,
        p.fecha_pedido,
        p.estado_pedido,
        pr.nombre_producto,
        dp.cantidad,
        dp.precio_unitario,
        (dp.cantidad * dp.precio_unitario) AS subtotal_linea,
        p.total AS total_pedido
    FROM usuarios u
    INNER JOIN pedidos p          ON u.id_usuario   = p.id_usuario
    INNER JOIN detalle_pedidos dp ON p.id_pedido    = dp.id_pedido
    INNER JOIN productos pr       ON dp.id_producto = pr.id_producto
    WHERE u.id_usuario = p_id_usuario
      AND p.fecha_pedido BETWEEN p_fecha_inicio AND p_fecha_fin
    ORDER BY p.fecha_pedido;
END$$

CREATE PROCEDURE sp_RegistrarPedidoCompleto(
    IN p_id_usuario  INT,
    IN p_id_producto INT,
    IN p_cantidad    INT
)
BEGIN
    DECLARE v_precio   DECIMAL(10,2) DEFAULT NULL;
    DECLARE v_stock    INT DEFAULT 0;
    DECLARE v_id_pedido INT;
    DECLARE v_total    DECIMAL(10,2);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT precio, inventario INTO v_precio, v_stock
    FROM productos
    WHERE id_producto = p_id_producto
    FOR UPDATE;

    IF v_precio IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El producto no existe.';
    END IF;

    IF v_stock < p_cantidad THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Inventario insuficiente para este producto.';
    END IF;

    SET v_total = v_precio * p_cantidad;

    INSERT INTO pedidos (id_usuario, estado_pedido, total)
    VALUES (p_id_usuario, 'Pendiente', v_total);

    SET v_id_pedido = LAST_INSERT_ID();

    INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario)
    VALUES (v_id_pedido, p_id_producto, p_cantidad, v_precio);

    UPDATE productos SET inventario = inventario - p_cantidad
    WHERE id_producto = p_id_producto;

    COMMIT;

    SELECT v_id_pedido AS id_pedido_generado, v_total AS total;
END$$

DELIMITER ;

-- ---------------------------------------------------------------------
-- Pruebas opcionales (quita los -- para ejecutarlas)
-- ---------------------------------------------------------------------
-- CALL sp_ReporteComprasUsuario(1, '2026-06-01', '2026-06-30');
-- CALL sp_RegistrarPedidoCompleto(2, 6, 3);
