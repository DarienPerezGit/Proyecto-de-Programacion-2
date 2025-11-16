-- ========================================
-- CABLEVISION HOLDING DATABASE
-- Exportado desde H2 y convertido a MySQL
-- ========================================

-- Eliminar base de datos si existe y crear nueva
DROP DATABASE IF EXISTS cablevision_holding;
CREATE DATABASE cablevision_holding CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cablevision_holding;

-- ========================================
-- CREAR TABLAS
-- ========================================

-- Tabla: AREA_MERCADO
CREATE TABLE area_mercado (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255),
    nombre VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: USUARIO
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(255),
    nombre VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: ASESOR (hereda de usuario)
CREATE TABLE asesor (
    codigo_asesor VARCHAR(255),
    titulacion VARCHAR(255),
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_asesor_usuario FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: PAIS
CREATE TABLE pais (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    capital VARCHAR(255),
    nombre VARCHAR(255),
    numero_habitantes BIGINT,
    pib DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: EMPRESA
CREATE TABLE empresa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    facturacion_anual DOUBLE,
    fecha_entrada_holding DATE,
    nombre VARCHAR(255),
    numero_vendedores INT,
    pais_sede_id BIGINT,
    ciudad_sede VARCHAR(255),
    CONSTRAINT fk_empresa_pais FOREIGN KEY (pais_sede_id) REFERENCES pais(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: VENDEDOR (hereda de usuario)
CREATE TABLE vendedor (
    codigo_vendedor VARCHAR(255),
    id BIGINT PRIMARY KEY,
    empresa_id BIGINT,
    vendedor_superior_id BIGINT,
    CONSTRAINT fk_vendedor_usuario FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_vendedor_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_vendedor_superior FOREIGN KEY (vendedor_superior_id) REFERENCES vendedor(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: ASESOR_EMPRESA_AREA
CREATE TABLE asesor_empresa_area (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio DATE,
    area_mercado_id BIGINT,
    asesor_id BIGINT,
    empresa_id BIGINT,
    CONSTRAINT fk_aea_area FOREIGN KEY (area_mercado_id) REFERENCES area_mercado(id),
    CONSTRAINT fk_aea_asesor FOREIGN KEY (asesor_id) REFERENCES asesor(id),
    CONSTRAINT fk_aea_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: CAPTACION
CREATE TABLE captacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_captacion DATE,
    vendedor_id BIGINT,
    vendedor_captado_id BIGINT,
    CONSTRAINT fk_captacion_vendedor FOREIGN KEY (vendedor_id) REFERENCES vendedor(id),
    CONSTRAINT fk_captacion_vendedor_captado FOREIGN KEY (vendedor_captado_id) REFERENCES vendedor(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: CUENTAS
CREATE TABLE cuentas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activa BOOLEAN NOT NULL,
    fecha_creacion DATETIME(6),
    numero_cuenta VARCHAR(255) NOT NULL UNIQUE,
    pin VARCHAR(255) NOT NULL,
    saldo DECIMAL(38, 2) NOT NULL,
    ultima_actualizacion DATETIME(6)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: DISPENSADORES
CREATE TABLE dispensadores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activo BOOLEAN NOT NULL,
    codigo VARCHAR(255) NOT NULL,
    saldo_disponible DECIMAL(38, 2) NOT NULL,
    ultima_recarga DATETIME(6),
    version BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: EMPRESA_AREA_MERCADO (relación muchos a muchos)
CREATE TABLE empresa_area_mercado (
    empresa_id BIGINT NOT NULL,
    area_mercado_id BIGINT NOT NULL,
    PRIMARY KEY (empresa_id, area_mercado_id),
    CONSTRAINT fk_eam_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_eam_area FOREIGN KEY (area_mercado_id) REFERENCES area_mercado(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: EMPRESA_PAIS_OPERACION (relación muchos a muchos)
CREATE TABLE empresa_pais_operacion (
    empresa_id BIGINT NOT NULL,
    pais_id BIGINT NOT NULL,
    PRIMARY KEY (empresa_id, pais_id),
    CONSTRAINT fk_epo_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_epo_pais FOREIGN KEY (pais_id) REFERENCES pais(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: TRANSACCIONES
CREATE TABLE transacciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255),
    exitosa BOOLEAN NOT NULL,
    fecha DATETIME(6) NOT NULL,
    monto DECIMAL(38, 2) NOT NULL,
    tipo VARCHAR(255) NOT NULL,
    cuenta_id BIGINT NOT NULL,
    CONSTRAINT fk_trans_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuentas(id),
    CONSTRAINT chk_tipo CHECK (tipo IN ('RETIRO', 'DEPOSITO', 'CONSULTA_SALDO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- INSERTAR DATOS
-- ========================================

-- Desactivar verificación de claves foráneas temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- Datos: AREA_MERCADO
INSERT INTO area_mercado (id, descripcion, nombre) VALUES
(1, 'Servicios de internet, telefonía y televisión por cable', 'Telecomunicaciones'),
(2, 'Plataformas de contenido digital y entretenimiento', 'Streaming y Entretenimiento'),
(3, 'Tecnología financiera y medios de pago', 'Fintech');

-- Datos: PAIS
INSERT INTO pais (id, capital, nombre, numero_habitantes, pib) VALUES
(1, 'Buenos Aires', 'Argentina', 45195777, 6.37485E11),
(2, 'Brasilia', 'Brasil', 215313498, 3.608438E12),
(3, 'Santiago', 'Chile', 19458310, 3.17057E11);

-- Datos: EMPRESA
INSERT INTO empresa (id, facturacion_anual, fecha_entrada_holding, nombre, numero_vendedores, pais_sede_id, ciudad_sede) VALUES
(1, 2.5E9, '2010-03-15', 'Cablevisión Argentina', 150, 1, 'Buenos Aires'),
(2, 8.0E8, '2015-07-22', 'Flow Entertainment', 75, 1, 'Córdoba'),
(3, 1.2E9, '2018-11-08', 'Telecom Brasil', 200, 2, 'São Paulo');

-- Datos: USUARIO
INSERT INTO usuario (id, direccion, nombre) VALUES
(1, 'Av. Corrientes 1234, CABA', 'Carlos Rodríguez'),
(2, 'Av. Santa Fe 5678, CABA', 'María González'),
(3, 'Av. Rivadavia 9876, CABA', 'Luis Pérez'),
(4, 'Av. Colón 456, Córdoba', 'Ana Martínez'),
(5, 'Av. Libertador 2468, CABA', 'Dr. Roberto Silva'),
(6, 'Av. 9 de Julio 1357, CABA', 'Lic. Patricia López');

-- Datos: VENDEDOR
INSERT INTO vendedor (codigo_vendedor, id, empresa_id, vendedor_superior_id) VALUES
('CV001', 1, 1, NULL),
('CV002', 2, 1, 1),
('CV003', 3, 1, 1),
('FL001', 4, 2, NULL);

-- Datos: ASESOR
INSERT INTO asesor (codigo_asesor, titulacion, id) VALUES
('AS001', 'MBA en Telecomunicaciones', 5),
('AS002', 'Lic. en Marketing Digital', 6);

-- Datos: ASESOR_EMPRESA_AREA
INSERT INTO asesor_empresa_area (id, fecha_inicio, area_mercado_id, asesor_id, empresa_id) VALUES
(1, '2023-01-10', 1, 5, 1),
(2, '2023-03-20', 2, 6, 2);

-- Datos: CAPTACION
INSERT INTO captacion (id, fecha_captacion, vendedor_id, vendedor_captado_id) VALUES
(1, '2023-05-15', 1, 2),
(2, '2023-08-22', 1, 3);

-- Datos: CUENTAS
INSERT INTO cuentas (id, activa, fecha_creacion, numero_cuenta, pin, saldo, ultima_actualizacion) VALUES
(1, TRUE, '2025-11-06 18:32:13.603488', '1234567890', '$2a$10$R2ZWKhXxtbnpkPqvVRBnV.jS9zxT02g.JzsykGULMo2j6OV1dbH7W', 5000.00, '2025-11-06 18:32:13.603488'),
(2, TRUE, '2025-11-06 18:32:13.886120', '9876543210', '$2a$10$qzxItT40rQPkoSTpDFJgnupvW2.BVcqMvEv83pXEaSUqPf3wsieDK', 10000.00, '2025-11-06 18:32:13.886120');

-- Datos: DISPENSADORES
INSERT INTO dispensadores (id, activo, codigo, saldo_disponible, ultima_recarga, version) VALUES
(1, TRUE, 'ATM001', 50000.00, '2025-11-06 18:32:13.176928', 0);

-- Datos: EMPRESA_AREA_MERCADO
INSERT INTO empresa_area_mercado (empresa_id, area_mercado_id) VALUES
(1, 1),
(2, 2),
(3, 1);

-- Datos: EMPRESA_PAIS_OPERACION
INSERT INTO empresa_pais_operacion (empresa_id, pais_id) VALUES
(1, 1),
(2, 1),
(3, 2);

-- Reactivar verificación de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- AJUSTAR AUTO_INCREMENT
-- ========================================
ALTER TABLE area_mercado AUTO_INCREMENT = 33;
ALTER TABLE usuario AUTO_INCREMENT = 33;
ALTER TABLE asesor_empresa_area AUTO_INCREMENT = 33;
ALTER TABLE captacion AUTO_INCREMENT = 33;
ALTER TABLE cuentas AUTO_INCREMENT = 3;
ALTER TABLE dispensadores AUTO_INCREMENT = 2;
ALTER TABLE pais AUTO_INCREMENT = 33;
ALTER TABLE empresa AUTO_INCREMENT = 33;

-- ========================================
-- ÍNDICES ADICIONALES
-- ========================================
CREATE INDEX idx_empresa_nombre ON empresa(nombre);
CREATE INDEX idx_pais_nombre ON pais(nombre);
CREATE INDEX idx_vendedor_codigo ON vendedor(codigo_vendedor);
CREATE INDEX idx_asesor_codigo ON asesor(codigo_asesor);
CREATE INDEX idx_transaccion_fecha ON transacciones(fecha);
CREATE INDEX idx_transaccion_tipo ON transacciones(tipo);

-- ========================================
-- VISTAS ÚTILES
-- ========================================

-- Vista de vendedores con información completa
CREATE OR REPLACE VIEW vista_vendedores AS
SELECT 
    v.id,
    u.nombre,
    u.direccion,
    v.codigo_vendedor,
    e.nombre AS empresa,
    vs.nombre AS vendedor_superior,
    (SELECT COUNT(*) FROM captacion c WHERE c.vendedor_id = v.id) AS total_captaciones
FROM vendedor v
INNER JOIN usuario u ON v.id = u.id
LEFT JOIN empresa e ON v.empresa_id = e.id
LEFT JOIN vendedor vs_id ON v.vendedor_superior_id = vs_id.id
LEFT JOIN usuario vs ON vs_id.id = vs.id;

-- Vista de empresas con información completa
CREATE OR REPLACE VIEW vista_empresas AS
SELECT 
    e.id,
    e.nombre,
    e.fecha_entrada_holding,
    e.facturacion_anual,
    e.numero_vendedores,
    e.ciudad_sede,
    p.nombre AS pais_sede,
    (SELECT COUNT(*) FROM vendedor v WHERE v.empresa_id = e.id) AS vendedores_registrados,
    (SELECT COUNT(*) FROM empresa_area_mercado eam WHERE eam.empresa_id = e.id) AS areas_mercado
FROM empresa e
LEFT JOIN pais p ON e.pais_sede_id = p.id;

-- Vista de resumen de cuentas
CREATE OR REPLACE VIEW vista_resumen_cuentas AS
SELECT 
    c.id,
    c.numero_cuenta,
    c.saldo,
    c.activa,
    COUNT(t.id) AS total_transacciones,
    SUM(CASE WHEN t.tipo = 'DEPOSITO' AND t.exitosa = TRUE THEN t.monto ELSE 0 END) AS total_depositos,
    SUM(CASE WHEN t.tipo = 'RETIRO' AND t.exitosa = TRUE THEN t.monto ELSE 0 END) AS total_retiros
FROM cuentas c
LEFT JOIN transacciones t ON c.id = t.cuenta_id
GROUP BY c.id, c.numero_cuenta, c.saldo, c.activa;

-- ========================================
-- CONSULTAS DE VERIFICACIÓN
-- ========================================

-- Verificar datos insertados
SELECT 'Áreas de Mercado' AS Tabla, COUNT(*) AS Total FROM area_mercado
UNION ALL SELECT 'Países', COUNT(*) FROM pais
UNION ALL SELECT 'Empresas', COUNT(*) FROM empresa
UNION ALL SELECT 'Usuarios', COUNT(*) FROM usuario
UNION ALL SELECT 'Vendedores', COUNT(*) FROM vendedor
UNION ALL SELECT 'Asesores', COUNT(*) FROM asesor
UNION ALL SELECT 'Cuentas', COUNT(*) FROM cuentas
UNION ALL SELECT 'Dispensadores', COUNT(*) FROM dispensadores
UNION ALL SELECT 'Captaciones', COUNT(*) FROM captacion;

-- ========================================
-- FIN DEL SCRIPT
-- ========================================
