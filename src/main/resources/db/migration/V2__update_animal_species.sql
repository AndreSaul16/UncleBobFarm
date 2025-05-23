-- Crear tabla temporal
CREATE TABLE animal_temp (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    especie ENUM('VACA', 'CABALLO', 'GALLINA') NOT NULL,
    edad INT NOT NULL,
    salud INT NOT NULL,
    ubicacion_id BIGINT,
    FOREIGN KEY (ubicacion_id) REFERENCES ubicacion(id)
);

-- Copiar datos existentes, convirtiendo especies inválidas a VACA
INSERT INTO animal_temp (id, nombre, especie, edad, salud, ubicacion_id)
SELECT 
    id,
    nombre,
    CASE 
        WHEN especie IN ('VACA', 'CABALLO', 'GALLINA') THEN especie
        ELSE 'VACA'
    END as especie,
    edad,
    salud,
    ubicacion_id
FROM animal;

-- Eliminar tabla original
DROP TABLE animal;

-- Renombrar tabla temporal
RENAME TABLE animal_temp TO animal; 