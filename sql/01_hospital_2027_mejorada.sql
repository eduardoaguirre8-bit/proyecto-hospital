-- =====================================================================
--  HOSPITAL 2027 · BASE DE DATOS MEJORADA
--  Compatible con MariaDB 10.4+ (XAMPP). Se puede ejecutar varias veces sin problema.
--  Funciona tanto si hospital_2027 ya existe (conserva tus datos) como si es nueva.
--
--  Cómo usarlo: phpMyAdmin > pestaña SQL (o Importar) > ejecutar este archivo.
--
--  Mejoras sobre tu estructura original:
--   1. Columna `pabellón` -> `pabellon` (sin tilde: evita problemas de codificación en consultas).
--   2. Campos obligatorios (nombres, apellidos, pabellon, consultorio, doctor) ahora son NOT NULL,
--      igual que exige el formulario del sistema.
--   3. Nueva columna fecha_registro (se llena sola) para poder hacer reportes de evolución.
--   4. Índices en pabellon, consultorio, doctor y apellidos: gráficos, reportes y búsquedas más rápidos.
--   5. Tabla `usuarios` (login con clave cifrada BCrypt) con usuario admin.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS hospital_2027
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE hospital_2027;

-- ---------------------------------------------------------------------
-- 1) TABLA PACIENTES (estructura final; si ya existe no se toca en este paso)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pacientes (
    ID_paciente    INT(11)      NOT NULL AUTO_INCREMENT,
    nombres        VARCHAR(30)  NOT NULL,
    apellidos      VARCHAR(30)  NOT NULL,
    pabellon       VARCHAR(30)  NOT NULL,
    consultorio    VARCHAR(100) NOT NULL,
    doctor         VARCHAR(50)  NOT NULL,
    obs            VARCHAR(100) DEFAULT NULL,
    fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ID_paciente)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ---------------------------------------------------------------------
-- 2) ACTUALIZAR UNA TABLA PACIENTES ANTIGUA (si ya la tenías con `pabellón`)
-- ---------------------------------------------------------------------
ALTER TABLE pacientes CHANGE COLUMN IF EXISTS `pabellón` pabellon VARCHAR(30) DEFAULT NULL;
ALTER TABLE pacientes ADD COLUMN IF NOT EXISTS fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Los registros antiguos sin dato se completan para poder exigir los campos obligatorios
UPDATE pacientes SET nombres     = 'Sin dato' WHERE nombres     IS NULL OR TRIM(nombres)     = '';
UPDATE pacientes SET apellidos   = 'Sin dato' WHERE apellidos   IS NULL OR TRIM(apellidos)   = '';
UPDATE pacientes SET pabellon    = 'Sin asignar' WHERE pabellon    IS NULL OR TRIM(pabellon)    = '';
UPDATE pacientes SET consultorio = 'Sin asignar' WHERE consultorio IS NULL OR TRIM(consultorio) = '';
UPDATE pacientes SET doctor      = 'Sin asignar' WHERE doctor      IS NULL OR TRIM(doctor)      = '';

ALTER TABLE pacientes
    MODIFY nombres     VARCHAR(30)  NOT NULL,
    MODIFY apellidos   VARCHAR(30)  NOT NULL,
    MODIFY pabellon    VARCHAR(30)  NOT NULL,
    MODIFY consultorio VARCHAR(100) NOT NULL,
    MODIFY doctor      VARCHAR(50)  NOT NULL;

-- ---------------------------------------------------------------------
-- 3) ÍNDICES (aceleran los GROUP BY de los gráficos y las búsquedas)
-- ---------------------------------------------------------------------
ALTER TABLE pacientes ADD INDEX IF NOT EXISTS idx_pacientes_pabellon (pabellon);
ALTER TABLE pacientes ADD INDEX IF NOT EXISTS idx_pacientes_consultorio (consultorio);
ALTER TABLE pacientes ADD INDEX IF NOT EXISTS idx_pacientes_doctor (doctor);
ALTER TABLE pacientes ADD INDEX IF NOT EXISTS idx_pacientes_apellidos (apellidos);
ALTER TABLE pacientes ADD INDEX IF NOT EXISTS idx_pacientes_fecha (fecha_registro);

-- ---------------------------------------------------------------------
-- 4) TABLA USUARIOS (login del sistema)
--    Usuario: admin   |   Clave: 123456   (guardada cifrada con BCrypt)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id             INT(11)      NOT NULL AUTO_INCREMENT,
    usuario        VARCHAR(50)  NOT NULL,
    clave          VARCHAR(100) NOT NULL,
    rol            VARCHAR(20)  NOT NULL DEFAULT 'ADMIN',
    activo         TINYINT(1)   NOT NULL DEFAULT 1,
    fecha_creacion DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_usuarios_usuario (usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS activo TINYINT(1) NOT NULL DEFAULT 1;
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

INSERT INTO usuarios (usuario, clave, rol, activo)
VALUES ('admin', '$2a$10$qEqkQt1.7Bf0sxnkj8N3bu9LBLPoRcM07AECmDvngT59WUVrrm1Ga', 'ADMIN', 1)
ON DUPLICATE KEY UPDATE clave = VALUES(clave), rol = VALUES(rol), activo = 1;

-- Listo: base hospital_2027 preparada para el sistema.
