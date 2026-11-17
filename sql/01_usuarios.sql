-- =====================================================================
-- Hospital 2027 · Tabla de usuarios para el login (ejecutar en hospital_2027)
-- Usuario: admin   |   Clave: 123456   (guardada con BCrypt, no en texto plano)
-- Tu tabla `pacientes` NO se modifica.
-- =====================================================================
USE hospital_2027;

CREATE TABLE IF NOT EXISTS usuarios (
    id      INT(11)      NOT NULL AUTO_INCREMENT,
    usuario VARCHAR(50)  NOT NULL,
    clave   VARCHAR(100) NOT NULL,
    rol     VARCHAR(20)  NOT NULL DEFAULT 'ADMIN',
    PRIMARY KEY (id),
    UNIQUE KEY uq_usuario (usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO usuarios (usuario, clave, rol)
VALUES ('admin', '$2a$10$qEqkQt1.7Bf0sxnkj8N3bu9LBLPoRcM07AECmDvngT59WUVrrm1Ga', 'ADMIN')
ON DUPLICATE KEY UPDATE clave = VALUES(clave), rol = VALUES(rol);
