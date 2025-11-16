-- ========================================
-- CABLEVISION HOLDING DATABASE SCHEMA
-- ========================================
-- Base de datos para Sistema de Gestión de Holding
-- Compatible con MySQL/MariaDB
-- ========================================

-- Eliminar tablas si existen (en orden inverso por dependencias)
DROP TABLE IF EXISTS transacciones;
DROP TABLE IF EXISTS dispensadores;
DROP TABLE IF EXISTS captacion;
DROP TABLE IF EXISTS asesor_empresa_area;
DROP TABLE IF EXISTS empresa_area_mercado;
DROP TABLE IF EXISTS empresa_pais_operacion;
DROP TABLE IF EXISTS vendedor;
DROP TABLE IF EXISTS asesor;
DROP TABLE IF EXISTS empresa;
DROP TABLE IF EXISTS area_mercado;
DROP TABLE IF EXISTS pais;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS cuentas;

-- ========================================
-- TABLA: usuario
-- ========================================
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(255),
    dtype VARCHAR(31) NOT NULL,
    CONSTRAINT chk_dtype CHECK (dtype IN ('Vendedor', 'Asesor'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: pais
-- ========================================
CREATE TABLE pais (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    pib DOUBLE,
    numero_habitantes BIGINT,
    capital VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: area_mercado
-- ========================================
CREATE TABLE area_mercado (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: empresa
-- ========================================
CREATE TABLE empresa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    fecha_entrada_holding DATE,
    facturacion_anual DOUBLE,
    numero_vendedores INT,
    ciudad_sede VARCHAR(255),
    pais_sede_id BIGINT,
    CONSTRAINT fk_empresa_pais FOREIGN KEY (pais_sede_id) REFERENCES pais(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: vendedor (hereda de usuario)
-- ========================================
CREATE TABLE vendedor (
    id BIGINT PRIMARY KEY,
    codigo_vendedor VARCHAR(100) UNIQUE,
    empresa_id BIGINT,
    vendedor_superior_id BIGINT,
    CONSTRAINT fk_vendedor_usuario FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_vendedor_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
    CONSTRAINT fk_vendedor_superior FOREIGN KEY (vendedor_superior_id) REFERENCES vendedor(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: asesor (hereda de usuario)
-- ========================================
CREATE TABLE asesor (
    id BIGINT PRIMARY KEY,
    codigo_asesor VARCHAR(100) UNIQUE,
    titulacion VARCHAR(255),
    CONSTRAINT fk_asesor_usuario FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: empresa_pais_operacion (relación muchos a muchos)
-- ========================================
CREATE TABLE empresa_pais_operacion (
    empresa_id BIGINT NOT NULL,
    pais_id BIGINT NOT NULL,
    PRIMARY KEY (empresa_id, pais_id),
    CONSTRAINT fk_emp_pais_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE,
    CONSTRAINT fk_emp_pais_pais FOREIGN KEY (pais_id) REFERENCES pais(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: empresa_area_mercado (relación muchos a muchos)
-- ========================================
CREATE TABLE empresa_area_mercado (
    empresa_id BIGINT NOT NULL,
    area_mercado_id BIGINT NOT NULL,
    PRIMARY KEY (empresa_id, area_mercado_id),
    CONSTRAINT fk_emp_area_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE,
    CONSTRAINT fk_emp_area_area FOREIGN KEY (area_mercado_id) REFERENCES area_mercado(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: asesor_empresa_area
-- ========================================
CREATE TABLE asesor_empresa_area (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asesor_id BIGINT NOT NULL,
    empresa_id BIGINT NOT NULL,
    area_mercado_id BIGINT NOT NULL,
    fecha_inicio DATE,
    CONSTRAINT fk_aea_asesor FOREIGN KEY (asesor_id) REFERENCES asesor(id) ON DELETE CASCADE,
    CONSTRAINT fk_aea_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE,
    CONSTRAINT fk_aea_area FOREIGN KEY (area_mercado_id) REFERENCES area_mercado(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: captacion
-- ========================================
CREATE TABLE captacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vendedor_id BIGINT NOT NULL,
    vendedor_captado_id BIGINT NOT NULL,
    fecha_captacion DATE NOT NULL,
    CONSTRAINT fk_captacion_vendedor FOREIGN KEY (vendedor_id) REFERENCES vendedor(id) ON DELETE CASCADE,
    CONSTRAINT fk_captacion_vendedor_captado FOREIGN KEY (vendedor_captado_id) REFERENCES vendedor(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: cuentas
-- ========================================
CREATE TABLE cuentas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta VARCHAR(50) NOT NULL UNIQUE,
    pin VARCHAR(255) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL,
    ultima_actualizacion DATETIME NOT NULL,
    CONSTRAINT chk_saldo_positivo CHECK (saldo >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: transacciones
-- ========================================
CREATE TABLE transacciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cuenta_id BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    monto DECIMAL(15,2) NOT NULL,
    fecha DATETIME NOT NULL,
    descripcion VARCHAR(500),
    exitosa BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_transaccion_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuentas(id) ON DELETE CASCADE,
    CONSTRAINT chk_tipo_transaccion CHECK (tipo IN ('RETIRO', 'DEPOSITO', 'CONSULTA_SALDO')),
    CONSTRAINT chk_monto_positivo CHECK (monto >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- TABLA: dispensadores
-- ========================================
CREATE TABLE dispensadores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    saldo_disponible DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    ultima_recarga DATETIME,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT DEFAULT 0,
    CONSTRAINT chk_saldo_dispensador_positivo CHECK (saldo_disponible >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- ÍNDICES ADICIONALES PARA MEJORAR RENDIMIENTO
-- ========================================

-- Índices para mejorar búsquedas frecuentes
CREATE INDEX idx_empresa_nombre ON empresa(nombre);
CREATE INDEX idx_pais_nombre ON pais(nombre);
CREATE INDEX idx_vendedor_codigo ON vendedor(codigo_vendedor);
CREATE INDEX idx_asesor_codigo ON asesor(codigo_asesor);
CREATE INDEX idx_cuenta_numero ON cuentas(numero_cuenta);
CREATE INDEX idx_transaccion_fecha ON transacciones(fecha);
CREATE INDEX idx_transaccion_tipo ON transacciones(tipo);

-- ========================================
-- DATOS DE EJEMPLO
-- ========================================

-- Insertar países
INSERT INTO pais (nombre, pib, numero_habitantes, capital) VALUES
('Argentina', 487227.0, 45376763, 'Buenos Aires'),
('Chile', 282318.0, 19116209, 'Santiago'),
('Uruguay', 56045.0, 3473727, 'Montevideo'),
('Paraguay', 38145.0, 7132530, 'Asunción'),
('Brasil', 1920096.0, 212559409, 'Brasilia'),
('México', 1293038.0, 128932753, 'Ciudad de México'),
('Colombia', 323803.0, 50882884, 'Bogotá'),
('Perú', 225408.0, 32971846, 'Lima');

-- Insertar áreas de mercado
INSERT INTO area_mercado (nombre, descripcion) VALUES
('Televisión por Cable', 'Servicios de televisión por cable y satelital'),
('Internet de Alta Velocidad', 'Servicios de conectividad de banda ancha'),
('Telefonía Móvil', 'Servicios de comunicación móvil'),
('Telefonía Fija', 'Servicios de telefonía fija tradicional'),
('Streaming Digital', 'Plataformas de contenido en streaming'),
('Servicios Empresariales', 'Soluciones de telecomunicaciones para empresas');

-- Insertar empresas
INSERT INTO empresa (nombre, fecha_entrada_holding, facturacion_anual, numero_vendedores, ciudad_sede, pais_sede_id) VALUES
('Cablevisión Argentina', '2018-03-15', 450000000.0, 150, 'Buenos Aires', 1),
('Telecom Chile', '2019-06-20', 380000000.0, 120, 'Santiago', 2),
('Conexión Uruguay', '2020-01-10', 95000000.0, 45, 'Montevideo', 3),
('Red Paraguay', '2020-09-05', 62000000.0, 30, 'Asunción', 4),
('Brasil Connect', '2021-02-18', 720000000.0, 250, 'São Paulo', 5);

-- Relacionar empresas con países de operación
INSERT INTO empresa_pais_operacion (empresa_id, pais_id) VALUES
(1, 1), (1, 3), -- Cablevisión Argentina opera en Argentina y Uruguay
(2, 2), (2, 4), -- Telecom Chile opera en Chile y Paraguay
(3, 3), (3, 1), -- Conexión Uruguay opera en Uruguay y Argentina
(4, 4), -- Red Paraguay opera en Paraguay
(5, 5), (5, 1), (5, 2); -- Brasil Connect opera en Brasil, Argentina y Chile

-- Relacionar empresas con áreas de mercado
INSERT INTO empresa_area_mercado (empresa_id, area_mercado_id) VALUES
(1, 1), (1, 2), (1, 4), -- Cablevisión: TV, Internet, Telefonía Fija
(2, 1), (2, 2), (2, 3), -- Telecom Chile: TV, Internet, Móvil
(3, 2), (3, 4), -- Conexión Uruguay: Internet, Telefonía Fija
(4, 1), (4, 2), -- Red Paraguay: TV, Internet
(5, 1), (5, 2), (5, 3), (5, 5); -- Brasil Connect: TV, Internet, Móvil, Streaming

-- Insertar usuarios (vendedores)
INSERT INTO usuario (nombre, direccion, dtype) VALUES
('Juan Pérez', 'Av. Corrientes 1234, Buenos Aires', 'Vendedor'),
('María González', 'Calle 50 #123, Santiago', 'Vendedor'),
('Carlos Rodríguez', '18 de Julio 1500, Montevideo', 'Vendedor'),
('Ana Martínez', 'Av. España 456, Asunción', 'Vendedor'),
('Luis Silva', 'Av. Paulista 1000, São Paulo', 'Vendedor'),
('Pedro Fernández', 'Av. Santa Fe 2000, Buenos Aires', 'Vendedor'),
('Laura Sánchez', 'Providencia 234, Santiago', 'Vendedor'),
('Jorge López', 'Colonia 567, Montevideo', 'Vendedor');

-- Insertar vendedores
INSERT INTO vendedor (id, codigo_vendedor, empresa_id, vendedor_superior_id) VALUES
(1, 'VEN-ARG-001', 1, NULL),
(2, 'VEN-CHI-001', 2, NULL),
(3, 'VEN-URU-001', 3, NULL),
(4, 'VEN-PAR-001', 4, NULL),
(5, 'VEN-BRA-001', 5, NULL),
(6, 'VEN-ARG-002', 1, 1),
(7, 'VEN-CHI-002', 2, 2),
(8, 'VEN-URU-002', 3, 3);

-- Insertar usuarios (asesores)
INSERT INTO usuario (nombre, direccion, dtype) VALUES
('Dr. Roberto Gutiérrez', 'Av. Libertador 789, Buenos Aires', 'Asesor'),
('Dra. Patricia Morales', 'Las Condes 890, Santiago', 'Asesor'),
('Lic. Miguel Herrera', 'Punta Carretas 321, Montevideo', 'Asesor');

-- Insertar asesores
INSERT INTO asesor (id, codigo_asesor, titulacion) VALUES
(9, 'ASE-001', 'MBA en Telecomunicaciones'),
(10, 'ASE-002', 'Doctorado en Ingeniería de Redes'),
(11, 'ASE-003', 'Licenciado en Administración de Empresas');

-- Relacionar asesores con empresas y áreas
INSERT INTO asesor_empresa_area (asesor_id, empresa_id, area_mercado_id, fecha_inicio) VALUES
(9, 1, 1, '2021-01-15'),
(9, 1, 2, '2021-01-15'),
(10, 2, 2, '2021-03-20'),
(10, 2, 3, '2021-03-20'),
(11, 3, 2, '2021-06-10'),
(11, 4, 1, '2021-08-05');

-- Insertar captaciones
INSERT INTO captacion (vendedor_id, vendedor_captado_id, fecha_captacion) VALUES
(1, 6, '2022-03-10'),
(2, 7, '2022-05-15'),
(3, 8, '2022-07-20');

-- Insertar cuentas
INSERT INTO cuentas (numero_cuenta, pin, saldo, activa, fecha_creacion, ultima_actualizacion) VALUES
('CTA-001-2024', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 50000.00, TRUE, NOW(), NOW()),
('CTA-002-2024', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 75000.00, TRUE, NOW(), NOW()),
('CTA-003-2024', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 100000.00, TRUE, NOW(), NOW()),
('CTA-004-2024', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 25000.00, TRUE, NOW(), NOW());

-- Insertar transacciones de ejemplo
INSERT INTO transacciones (cuenta_id, tipo, monto, fecha, descripcion, exitosa) VALUES
(1, 'DEPOSITO', 10000.00, NOW(), 'Depósito inicial', TRUE),
(1, 'RETIRO', 5000.00, NOW(), 'Retiro en cajero', TRUE),
(2, 'DEPOSITO', 25000.00, NOW(), 'Depósito inicial', TRUE),
(3, 'CONSULTA_SALDO', 0.00, NOW(), 'Consulta de saldo', TRUE),
(2, 'RETIRO', 10000.00, NOW(), 'Retiro en sucursal', TRUE);

-- Insertar dispensadores
INSERT INTO dispensadores (codigo, saldo_disponible, ultima_recarga, activo, version) VALUES
('DISP-001', 500000.00, NOW(), TRUE, 0),
('DISP-002', 750000.00, NOW(), TRUE, 0),
('DISP-003', 300000.00, NOW(), TRUE, 0),
('DISP-004', 1000000.00, NOW(), FALSE, 0);

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

-- Vista de asesores con información completa
CREATE OR REPLACE VIEW vista_asesores AS
SELECT 
    a.id,
    u.nombre,
    u.direccion,
    a.codigo_asesor,
    a.titulacion,
    COUNT(aea.id) AS total_asignaciones
FROM asesor a
INNER JOIN usuario u ON a.id = u.id
LEFT JOIN asesor_empresa_area aea ON a.id = aea.asesor_id
GROUP BY a.id, u.nombre, u.direccion, a.codigo_asesor, a.titulacion;

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
-- FIN DEL SCRIPT
-- ========================================
