-- =====================================================================
-- CapZone - Base de datos (SQL Server)
-- Ejecutar COMPLETO en SSMS (F5).
-- ADVERTENCIA: si capzone_db ya existe, se borra y se vuelve a crear.
-- =====================================================================

IF DB_ID('capzone_db') IS NOT NULL
BEGIN
    ALTER DATABASE capzone_db SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE capzone_db;
END
GO

CREATE DATABASE capzone_db;
GO

USE capzone_db;
GO

-- ---------------------------------------------------------------------
-- Tablas
-- ---------------------------------------------------------------------
CREATE TABLE usuarios (
    id_usuario      INT PRIMARY KEY IDENTITY(1,1),
    nombre_completo VARCHAR(100) NOT NULL,
    correo          VARCHAR(50)  NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,   
    telefono        VARCHAR(15)  NOT NULL,
    fecha_registro  DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE categorias (
    id_categoria     INT PRIMARY KEY IDENTITY(1,1),
    nombre_categoria VARCHAR(50) NOT NULL UNIQUE,
    descripcion      VARCHAR(500) NULL
);
GO

CREATE TABLE productos (
    id_producto     INT PRIMARY KEY IDENTITY(1,1),
    id_categoria    INT NOT NULL FOREIGN KEY REFERENCES categorias(id_categoria),
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion     VARCHAR(500) NULL,
    precio          DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    inventario      INT NOT NULL CHECK (inventario >= 0),
    url_imagen      VARCHAR(255) NULL
);
GO

CREATE TABLE pedidos (
    id_pedido     INT PRIMARY KEY IDENTITY(1,1),
    id_usuario    INT NOT NULL FOREIGN KEY REFERENCES usuarios(id_usuario),
    fecha_pedido  DATETIME DEFAULT GETDATE(),
    estado_pedido VARCHAR(20) NOT NULL DEFAULT 'Pendiente'
        CONSTRAINT CK_pedidos_estado CHECK (estado_pedido IN ('Pendiente','Confirmado','Enviado','Entregado')),
    total         DECIMAL(10,2) NOT NULL
);
GO

CREATE TABLE detalle_pedidos (
    id_detalle      INT PRIMARY KEY IDENTITY(1,1),
    id_pedido       INT NOT NULL FOREIGN KEY REFERENCES pedidos(id_pedido),
    id_producto     INT NOT NULL FOREIGN KEY REFERENCES productos(id_producto),
    cantidad        INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL
);
GO

CREATE TABLE mensajes_contacto (
    id_contacto INT PRIMARY KEY IDENTITY(1,1),
    nombre      VARCHAR(100) NOT NULL,
    correo      VARCHAR(50)  NOT NULL,
    asunto      VARCHAR(100) NULL,                 -- nuevo: lo usa la clase MensajeContacto
    mensaje     VARCHAR(1000) NOT NULL,
    fecha_envio DATETIME DEFAULT GETDATE(),
    leido       BIT NOT NULL DEFAULT 0             -- nuevo: lo usa la clase MensajeContacto
);
GO

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
GO

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
GO

INSERT INTO usuarios (nombre_completo, correo, password, telefono) VALUES
('Juan Pérez Gómez',      'juan.perez@email.com',   '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '3157778899'),
('María Camila Restrepo', 'maria.camila@email.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '3004445566'),
('Carlos Mario Giraldo',  'carlos.mario@email.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '3102221100'),
('Sergio Castaño',        'sergio@capzone.com',     '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '3000000000');
GO

INSERT INTO pedidos (id_usuario, fecha_pedido, estado_pedido, total) VALUES
(1, '2026-06-15 10:30:00', 'Confirmado', 254800.00),
(2, '2026-06-20 16:45:00', 'Enviado',    140000.00),
(1, '2026-06-25 09:15:00', 'Pendiente',   45000.00);
GO

INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario) VALUES
(1, 1, 2, 89900.00),
(1, 4, 1, 75000.00),
(2, 6, 1, 68000.00),
(2, 8, 1, 72000.00),
(3, 9, 1, 45000.00);
GO

INSERT INTO mensajes_contacto (nombre, correo, asunto, mensaje) VALUES
('Andrés Mendoza', 'andres.m@email.com', 'Compra al por mayor', 'Hola, me interesa comprar al por mayor de la gorra Trucker Dark All-Black.'),
('Diana Tobón',    'diana.t@email.com',  'Envíos',              '¿Hacen envíos a municipios fuera de Medellín?');
GO

-- ---------------------------------------------------------------------
-- Procedimientos almacenados
-- (La app Java registra los pedidos con su propia transacción para poder
--  comprar varios productos a la vez; este SP queda como versión de 1 producto.)
-- ---------------------------------------------------------------------
CREATE PROCEDURE sp_ReporteComprasUsuario
    @id_usuario   INT,
    @fecha_inicio DATETIME,
    @fecha_fin    DATETIME
AS
BEGIN
    SET NOCOUNT ON;

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
    INNER JOIN pedidos p          ON u.id_usuario  = p.id_usuario
    INNER JOIN detalle_pedidos dp ON p.id_pedido   = dp.id_pedido
    INNER JOIN productos pr       ON dp.id_producto = pr.id_producto
    WHERE u.id_usuario = @id_usuario
      AND p.fecha_pedido BETWEEN @fecha_inicio AND @fecha_fin
    ORDER BY p.fecha_pedido;
END
GO

CREATE PROCEDURE sp_RegistrarPedidoCompleto
    @id_usuario  INT,
    @id_producto INT,
    @cantidad    INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @precio DECIMAL(10,2);
        DECLARE @stock_disponible INT;
        DECLARE @nuevo_id_pedido INT;
        DECLARE @total_calculado DECIMAL(10,2);

        SELECT @precio = precio, @stock_disponible = inventario
        FROM productos
        WHERE id_producto = @id_producto;

        IF @precio IS NULL
            RAISERROR('El producto no existe.', 16, 1);

        IF @stock_disponible < @cantidad
            RAISERROR('Inventario insuficiente para este producto.', 16, 1);

        SET @total_calculado = @precio * @cantidad;

        INSERT INTO pedidos (id_usuario, estado_pedido, total)
        VALUES (@id_usuario, 'Pendiente', @total_calculado);

        SET @nuevo_id_pedido = SCOPE_IDENTITY();

        INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario)
        VALUES (@nuevo_id_pedido, @id_producto, @cantidad, @precio);

        UPDATE productos
        SET inventario = inventario - @cantidad
        WHERE id_producto = @id_producto;

        COMMIT TRANSACTION;

        SELECT @nuevo_id_pedido AS id_pedido_generado, @total_calculado AS total;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        SELECT ERROR_MESSAGE() AS mensaje_error;
    END CATCH
END
GO

-- ---------------------------------------------------------------------
-- Pruebas opcionales (quita los -- para ejecutarlas)
-- ---------------------------------------------------------------------
 EXEC sp_ReporteComprasUsuario @id_usuario = 1, @fecha_inicio = '2026-06-01', @fecha_fin = '2026-06-30';
 EXEC sp_RegistrarPedidoCompleto @id_usuario = 2, @id_producto = 6, @cantidad = 3;

 --Ranking de clientes--
 SELECT u.nombre_completo, u.correo,
        COUNT(p.id_pedido) AS cantidad_pedidos,
        SUM(p.total)       AS total_comprado
 FROM usuarios u
 LEFT JOIN pedidos p ON u.id_usuario = p.id_usuario
 GROUP BY u.nombre_completo, u.correo
 ORDER BY total_comprado DESC;
GO


select productos.nombre_producto, categorias.nombre_categoria, productos.precio, productos.inventario
from productos
inner join categorias on productos.id_categoria = categorias.id_categoria
where productos.inventario < 20
go