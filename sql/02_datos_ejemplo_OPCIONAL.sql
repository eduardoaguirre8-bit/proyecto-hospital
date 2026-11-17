-- =====================================================================
-- OPCIONAL: pacientes de prueba para ver los gráficos con datos.
-- Ejecútalo DESPUÉS de 01_hospital_2027_mejorada.sql, y solo si tu tabla está vacía o quieres probar. Puedes borrarlos luego desde el sistema.
-- =====================================================================
USE hospital_2027;

INSERT INTO pacientes (nombres, apellidos, pabellon, consultorio, doctor, obs) VALUES
('María José',  'Quispe Ramos',   'Pabellón A', 'Consultorio 101', 'Dr. Ramírez',  'Control post-operatorio'),
('Luis Alberto','Torres Díaz',    'Pabellón A', 'Consultorio 101', 'Dr. Ramírez',  'Alergia a la penicilina'),
('Rosa',        'Huamán Flores',  'Pabellón B', 'Consultorio 203', 'Dra. Núñez',   'Seguimiento de hipertensión'),
('Carlos',      'Mendoza Ríos',   'Pabellón B', 'Consultorio 203', 'Dra. Núñez',   NULL),
('Ana Lucía',   'Paredes Vega',   'Pabellón C', 'Consultorio 305', 'Dr. Quispe',   'Revisión de rutina'),
('Jorge',       'Salazar Cruz',   'Emergencia', 'Consultorio 001', 'Dra. Torres',  'Ingreso por fractura'),
('Elena',       'Vargas Soto',    'Pabellón A', 'Consultorio 102', 'Dr. Ramírez',  'Exámenes pendientes'),
('Miguel',      'Castillo Rojas', 'Pabellón B', 'Consultorio 204', 'Dr. Quispe',   NULL),
('Patricia',    'Lozano Gómez',   'Pabellón C', 'Consultorio 305', 'Dra. Núñez',   'Control prenatal'),
('Diego',       'Ccori Mamani',   'Emergencia', 'Consultorio 001', 'Dra. Torres',  'Observación 24 h'),
('Sofía',       'Ramos Ortiz',    'Pabellón A', 'Consultorio 101', 'Dr. Quispe',   'Alta programada'),
('Renzo',       'Aguirre León',   'Pabellón B', 'Consultorio 203', 'Dr. Ramírez',  'Terapia física');
