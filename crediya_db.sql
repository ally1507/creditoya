-- =====================================================================
-- CrediYa S.A.S. - Base de datos completa (version actual)
-- Ejecutar en MySQL Workbench con el usuario root.
-- OJO: borra la base crediya_db si ya existe y la crea de nuevo vacia,
-- solo con el administrador (documento 1000000001, clave admin123).
-- =====================================================================

DROP DATABASE IF EXISTS crediya_db;
CREATE DATABASE crediya_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE crediya_db;

-- Usuario que usa el programa (database/Conexion.java): crediya / 1234
CREATE USER IF NOT EXISTS 'crediya'@'localhost' IDENTIFIED BY '1234';
GRANT SELECT, INSERT, UPDATE, DELETE ON crediya_db.* TO 'crediya'@'localhost';

-- Datos comunes de empleados y clientes
CREATE TABLE personas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL,
  documento VARCHAR(15) NOT NULL UNIQUE,
  correo VARCHAR(80) NOT NULL UNIQUE,
  telefono VARCHAR(10) NULL,
  rol ENUM('ADMIN','ASESOR','CLIENTE') NOT NULL,
  clave_hash CHAR(60) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE empleados (
  persona_id INT PRIMARY KEY,
  salario DECIMAL(12,2) NOT NULL,
  CONSTRAINT chk_empleado_salario CHECK (salario > 0),
  FOREIGN KEY (persona_id) REFERENCES personas(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE clientes (
  persona_id INT PRIMARY KEY,
  direccion VARCHAR(120) NOT NULL,
  FOREIGN KEY (persona_id) REFERENCES personas(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE solicitudes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cliente_id INT NOT NULL,
  empleado_id INT NOT NULL,
  monto DECIMAL(12,2) NOT NULL,
  cuotas INT NOT NULL,
  fecha_solicitud DATE NOT NULL,
  estado ENUM('PENDIENTE','APROBADA','RECHAZADA') NOT NULL DEFAULT 'PENDIENTE',
  observaciones VARCHAR(255) NULL,
  CONSTRAINT chk_solicitud_monto CHECK (monto > 0),
  CONSTRAINT chk_solicitud_cuotas CHECK (cuotas BETWEEN 1 AND 120),
  UNIQUE KEY uk_solicitud_estado (id, estado),
  INDEX idx_solicitudes_estado (estado),
  FOREIGN KEY (cliente_id) REFERENCES clientes(persona_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (empleado_id) REFERENCES empleados(persona_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Un prestamo solo puede salir de una solicitud APROBADA (llave compuesta)
CREATE TABLE prestamos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cliente_id INT NOT NULL,
  empleado_id INT NOT NULL,
  solicitud_id INT NOT NULL UNIQUE,
  solicitud_estado ENUM('PENDIENTE','APROBADA','RECHAZADA') AS ('APROBADA') STORED,
  monto DECIMAL(12,2) NOT NULL,
  tasa_ea DECIMAL(7,4) NOT NULL,
  tasa_mensual DECIMAL(9,6) NOT NULL,
  cuotas INT NOT NULL,
  total_pagar DECIMAL(14,2) NOT NULL,
  fecha_inicio DATE NOT NULL,
  fecha_fin DATE NOT NULL,
  estado ENUM('PENDIENTE','ACTIVO','PAGADO','VENCIDO') NOT NULL DEFAULT 'PENDIENTE',
  CONSTRAINT chk_prestamo_monto CHECK (monto > 0),
  CONSTRAINT chk_prestamo_tasa CHECK (tasa_ea >= 0 AND tasa_mensual >= 0),
  CONSTRAINT chk_prestamo_cuotas CHECK (cuotas BETWEEN 1 AND 120),
  CONSTRAINT chk_prestamo_total CHECK (total_pagar >= monto),
  CONSTRAINT chk_prestamo_fechas CHECK (fecha_fin > fecha_inicio),
  INDEX idx_prestamos_fecha (fecha_inicio),
  INDEX idx_prestamos_estado (estado),
  FOREIGN KEY (cliente_id) REFERENCES clientes(persona_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (empleado_id) REFERENCES empleados(persona_id) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (solicitud_id, solicitud_estado) REFERENCES solicitudes(id, estado)
    ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Plan de cuotas: una fila por cuota
CREATE TABLE cuotas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  prestamo_id INT NOT NULL,
  numero INT NOT NULL,
  fecha_vencimiento DATE NOT NULL,
  capital DECIMAL(12,2) NOT NULL,
  interes DECIMAL(12,2) NOT NULL,
  valor DECIMAL(12,2) NOT NULL,
  abonado DECIMAL(12,2) NOT NULL DEFAULT 0,
  saldo DECIMAL(14,2) NOT NULL,
  estado ENUM('PENDIENTE','PARCIAL','PAGADA','VENCIDA') NOT NULL DEFAULT 'PENDIENTE',
  UNIQUE KEY uk_cuota_numero (prestamo_id, numero),
  CONSTRAINT chk_cuota_numero CHECK (numero > 0),
  CONSTRAINT chk_cuota_valores CHECK (capital >= 0 AND interes >= 0 AND valor > 0 AND saldo >= 0),
  CONSTRAINT chk_cuota_abonado CHECK (abonado >= 0 AND abonado <= valor),
  INDEX idx_cuotas_vencimiento (estado, fecha_vencimiento),
  FOREIGN KEY (prestamo_id) REFERENCES prestamos(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE pagos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  prestamo_id INT NOT NULL,
  empleado_id INT NOT NULL,
  fecha_pago DATE NOT NULL,
  monto DECIMAL(12,2) NOT NULL,
  metodo ENUM('EFECTIVO','TRANSFERENCIA','TARJETA') NOT NULL,
  observaciones VARCHAR(255) NULL,
  CONSTRAINT chk_pago_monto CHECK (monto > 0),
  INDEX idx_pagos_fecha (fecha_pago),
  FOREIGN KEY (prestamo_id) REFERENCES prestamos(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (empleado_id) REFERENCES empleados(persona_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Administrador inicial: documento 1000000001, clave admin123
INSERT INTO personas (nombre, documento, correo, telefono, rol, clave_hash)
VALUES ('Administrador', '1000000001', 'admin@crediya.com', '3000000000', 'ADMIN', 'admin123');

INSERT INTO empleados (persona_id, salario) VALUES (LAST_INSERT_ID(), 3000000.00);

-- Comprobacion: debe mostrar al administrador
SELECT p.id, p.nombre, p.documento, p.clave_hash, p.rol, e.salario
FROM personas p JOIN empleados e ON e.persona_id = p.id;
