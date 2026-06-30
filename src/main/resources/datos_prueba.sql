-- =============================================================================
-- DATOS DE PRUEBA - Epic 1: Propiedades
-- Correr manualmente en MySQL Workbench sobre la base desi2026
-- DESPUÉS de levantar la app al menos una vez (para que Hibernate cree las tablas)
-- =============================================================================

-- Provincias (si no existen)
INSERT IGNORE INTO provincia (nombre) VALUES ('Buenos Aires');
INSERT IGNORE INTO provincia (nombre) VALUES ('Córdoba');
INSERT IGNORE INTO provincia (nombre) VALUES ('Santa Fe');

-- Ciudades (si no existen)
INSERT IGNORE INTO ciudad (nombre, provincia_id)
    VALUES ('La Plata',      (SELECT id FROM provincia WHERE nombre = 'Buenos Aires' LIMIT 1));
INSERT IGNORE INTO ciudad (nombre, provincia_id)
    VALUES ('Mar del Plata', (SELECT id FROM provincia WHERE nombre = 'Buenos Aires' LIMIT 1));
INSERT IGNORE INTO ciudad (nombre, provincia_id)
    VALUES ('Córdoba',       (SELECT id FROM provincia WHERE nombre = 'Córdoba' LIMIT 1));
INSERT IGNORE INTO ciudad (nombre, provincia_id)
    VALUES ('Rosario',       (SELECT id FROM provincia WHERE nombre = 'Santa Fe' LIMIT 1));

-- Personas propietarias (si no existen)
INSERT IGNORE INTO personas (nombre, apellido, dni, telefono, email, eliminado, id_Ciudad)
    VALUES ('Ana',    'González', '30111222', '221-4001001', 'ana.gonzalez@mail.com',   false,
            (SELECT id FROM ciudad WHERE nombre = 'La Plata' LIMIT 1));
INSERT IGNORE INTO personas (nombre, apellido, dni, telefono, email, eliminado, id_Ciudad)
    VALUES ('Marcos', 'Pérez',    '28555444', '221-4002002', 'marcos.perez@mail.com',   false,
            (SELECT id FROM ciudad WHERE nombre = 'La Plata' LIMIT 1));
INSERT IGNORE INTO personas (nombre, apellido, dni, telefono, email, eliminado, id_Ciudad)
    VALUES ('Laura',  'Sánchez',  '33999888', '351-5003003', 'laura.sanchez@mail.com',  false,
            (SELECT id FROM ciudad WHERE nombre = 'Córdoba' LIMIT 1));

-- Propiedades de prueba
-- Nota: propietario_id referencia a la tabla Personas; ajustar IDs si la BD ya tiene filas.
INSERT INTO propiedad (direccion, ciudad, tipo, cant_ambientes, mts_cuadrados,
                       descripcion, comodidades, estado_disp, eliminada, propietario_id)
    VALUES ('Calle 7 Nro 1234', 'La Plata', 'CASA', 4, 120.00,
            'Casa amplia en zona residencial, a 3 cuadras del parque.',
            'Garage doble, jardín, parrilla, aire acondicionado central.',
            'DISPONIBLE', false,
            (SELECT id FROM personas WHERE dni = '30111222' LIMIT 1));

INSERT INTO propiedad (direccion, ciudad, tipo, cant_ambientes, mts_cuadrados,
                       descripcion, comodidades, estado_disp, eliminada, propietario_id)
    VALUES ('Av. 51 Nro 890', 'La Plata', 'DEPARTAMENTO', 2, 55.00,
            'Departamento luminoso en planta baja con entrada propia.',
            'Balcón, cocina equipada, calefacción central.',
            'DISPONIBLE', false,
            (SELECT id FROM personas WHERE dni = '28555444' LIMIT 1));

INSERT INTO propiedad (direccion, ciudad, tipo, cant_ambientes, mts_cuadrados,
                       descripcion, comodidades, estado_disp, eliminada, propietario_id)
    VALUES ('San Martín 456', 'Córdoba', 'LOCAL', 1, 80.00,
            'Local comercial en galería céntrica, alta circulación peatonal.',
            'Vidrieras amplias, depósito trasero, baño independiente.',
            'ALQUILADA', false,
            (SELECT id FROM personas WHERE dni = '33999888' LIMIT 1));

INSERT INTO propiedad (direccion, ciudad, tipo, cant_ambientes, mts_cuadrados,
                       descripcion, comodidades, estado_disp, eliminada, propietario_id)
    VALUES ('Italia 789', 'Rosario', 'DEPARTAMENTO', 3, 90.00,
            'Departamento en piso alto con vista al río.',
            'Pileta en edificio, SUM, seguridad 24hs, cochera.',
            'RESERVADA', false,
            (SELECT id FROM personas WHERE dni = '28555444' LIMIT 1));

-- Historial inicial para cada propiedad (primera entrada = estado al momento del alta)
-- Se insertan manualmente porque la app lo hace por código al crear desde el formulario.
-- Solo es necesario si se insertan propiedades directo por SQL (saltando el formulario).
INSERT INTO historial_estado_propiedad (estado, fecha_hora, propiedad_id)
    SELECT 'DISPONIBLE', NOW(), id FROM propiedad WHERE direccion = 'Calle 7 Nro 1234' AND ciudad = 'La Plata';

INSERT INTO historial_estado_propiedad (estado, fecha_hora, propiedad_id)
    SELECT 'DISPONIBLE', NOW(), id FROM propiedad WHERE direccion = 'Av. 51 Nro 890' AND ciudad = 'La Plata';

INSERT INTO historial_estado_propiedad (estado, fecha_hora, propiedad_id)
    SELECT 'ALQUILADA', NOW(), id FROM propiedad WHERE direccion = 'San Martín 456' AND ciudad = 'Córdoba';

INSERT INTO historial_estado_propiedad (estado, fecha_hora, propiedad_id)
    SELECT 'RESERVADA', NOW(), id FROM propiedad WHERE direccion = 'Italia 789' AND ciudad = 'Rosario';

-- =============================================================================
-- FIN DEL SCRIPT
-- Para verificar: SELECT * FROM propiedad; SELECT * FROM historial_estado_propiedad;
-- =============================================================================
