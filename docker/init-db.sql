-- Initialize database with base data
USE gestorgranja;

-- Create initial employees (if they don't exist)
INSERT IGNORE INTO empleado (id, nombre, apellido, rol, username, password, email)
VALUES 
(1, 'Admin', 'System', 'ADMIN', 'admin', '$2a$10$zRJmZ3ZSaXlpCIhns/8E9Oirg9nHUvqIj1R7JN.VZO3WlGszSFDvC', 'admin@unclebobfarm.com'),
(2, 'User', 'Regular', 'EMPLEADO', 'user', '$2a$10$MIJes5qoNZTCqYlvg2qXI.sFYp5YTCaHEcgVFm9KetFDMSZsC9nm2', 'user@unclebobfarm.com');

-- Initialize inventory types
INSERT IGNORE INTO inventario (id, nombre, descripcion, tipo, cantidad, fecha_compra)
VALUES
(1, 'Alimento estándar', 'Alimentación general para ganado', 'ALIMENTO', 100, '2023-01-01'),
(2, 'Vacuna general', 'Vacuna para prevención de enfermedades comunes', 'MEDICINA', 50, '2023-01-15'),
(3, 'Kit de limpieza', 'Material básico para limpieza de establos', 'MATERIAL_LIMPIEZA', 30, '2023-02-01');

-- Initialize locations
INSERT IGNORE INTO ubicacion (id, nombre, descripcion, capacidad, tipo)
VALUES
(1, 'Establo Principal', 'Establo principal de la granja', 20, 'ESTABLO'),
(2, 'Corral Norte', 'Corral área norte', 30, 'CORRAL'),
(3, 'Corral Sur', 'Corral área sur', 25, 'CORRAL');

-- Sample animals (optional)
INSERT IGNORE INTO animal (id, nombre, especie, fecha_nacimiento, peso, estado_salud, ubicacion_id)
VALUES
(1, 'Bella', 'VACA', '2020-05-15', 450.5, 90, 1),
(2, 'Rocky', 'TORO', '2019-07-20', 600.2, 85, 2),
(3, 'Daisy', 'VACA', '2021-03-10', 380.0, 95, 1);
