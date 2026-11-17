-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 16-11-2026 a las 16:50:24
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `hospital_2027`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pacientes`
--

CREATE TABLE `pacientes` (
  `ID_paciente` int(11) NOT NULL,
  `nombres` varchar(30) NOT NULL,
  `apellidos` varchar(30) NOT NULL,
  `pabellon` varchar(30) NOT NULL,
  `consultorio` varchar(100) NOT NULL,
  `doctor` varchar(50) NOT NULL,
  `obs` varchar(100) DEFAULT NULL,
  `fecha_registro` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pacientes`
--

INSERT INTO `pacientes` (`ID_paciente`, `nombres`, `apellidos`, `pabellon`, `consultorio`, `doctor`, `obs`, `fecha_registro`) VALUES
(1, 'Daniel', 'Valderrama', 'Pabellon D', 'Consultorio 102', 'Dr Chapatin', 'El paciente tiene fiebre', '2026-11-16 10:44:02'),
(2, 'Christiana', 'Flores', 'Pabellon A', 'Consultorio 101', 'Dr Forex', 'Paciente Fallecido', '2026-11-16 10:46:41'),
(3, 'Miguel', 'Arcángel', 'Pabellon C', 'Consultorio 100', 'Dr Morris', 'Paciente con asma', '2026-11-16 10:48:03'),
(4, 'Liria', 'Seminario', 'Pabellon A', 'Consultorio 101', 'Dr Forex', 'Paciente con una fuerte fiebre presenta problemas respiratorios aun en revisión', '2026-11-16 10:49:38');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `clave` varchar(100) NOT NULL,
  `rol` varchar(20) NOT NULL DEFAULT 'ADMIN',
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `fecha_creacion` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `usuario`, `clave`, `rol`, `activo`, `fecha_creacion`) VALUES
(1, 'admin', '$2a$10$qEqkQt1.7Bf0sxnkj8N3bu9LBLPoRcM07AECmDvngT59WUVrrm1Ga', 'ADMIN', 1, '2026-11-16 10:41:00');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `pacientes`
--
ALTER TABLE `pacientes`
  ADD PRIMARY KEY (`ID_paciente`),
  ADD KEY `idx_pacientes_pabellon` (`pabellon`),
  ADD KEY `idx_pacientes_consultorio` (`consultorio`),
  ADD KEY `idx_pacientes_doctor` (`doctor`),
  ADD KEY `idx_pacientes_apellidos` (`apellidos`),
  ADD KEY `idx_pacientes_fecha` (`fecha_registro`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_usuarios_usuario` (`usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `pacientes`
--
ALTER TABLE `pacientes`
  MODIFY `ID_paciente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
