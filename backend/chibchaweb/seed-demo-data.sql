-- ============================================================
-- SCRIPT DE DATOS DE DEMOSTRACIÓN — ChibchaWeb Colombia
-- Ejecutar una sola vez tras iniciar la aplicación.
-- Es seguro re-ejecutarlo (usa ON CONFLICT).
-- ============================================================
BEGIN;

-- ============================================================
-- 1. PLANES DE HOSTING (SINGLE_TABLE)
-- ============================================================
INSERT INTO app.hosting_plan (id, tipo_plan, nombre, precio_mensual, espacio_disco, ancho_banda, cuentas_email, limite_sitios, mysql_incluido, php_version)
VALUES (1, 'PLATINO_UNIX', 'Platino Unix', 89900, 20480, 0, 50, 10, true, '8.2')
ON CONFLICT (id) DO NOTHING;

INSERT INTO app.hosting_plan (id, tipo_plan, nombre, precio_mensual, espacio_disco, ancho_banda, cuentas_email, limite_sitios, sql_server_incluido, iis_version)
VALUES (2, 'PLATINO_WINDOWS', 'Platino Windows', 99900, 20480, 0, 50, 10, true, '10.0')
ON CONFLICT (id) DO NOTHING;

INSERT INTO app.hosting_plan (id, tipo_plan, nombre, precio_mensual, espacio_disco, ancho_banda, cuentas_email, limite_sitios, python_incluido)
VALUES (3, 'ORO_UNIX', 'Oro Unix', 49900, 10240, 512000, 20, 5, true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO app.hosting_plan (id, tipo_plan, nombre, precio_mensual, espacio_disco, ancho_banda, cuentas_email, limite_sitios, asp_net_version)
VALUES (4, 'ORO_WINDOWS', 'Oro Windows', 59900, 10240, 512000, 20, 5, '8.0')
ON CONFLICT (id) DO NOTHING;

INSERT INTO app.hosting_plan (id, tipo_plan, nombre, precio_mensual, espacio_disco, ancho_banda, cuentas_email, limite_sitios)
VALUES (5, 'PLATA_UNIX', 'Plata Unix', 24900, 5120, 102400, 5, 3)
ON CONFLICT (id) DO NOTHING;

INSERT INTO app.hosting_plan (id, tipo_plan, nombre, precio_mensual, espacio_disco, ancho_banda, cuentas_email, limite_sitios)
VALUES (6, 'PLATA_WINDOWS', 'Plata Windows', 29900, 5120, 102400, 5, 3)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 2. USUARIOS (tabla padre JOINED)
-- ============================================================
INSERT INTO app.usuario (id, nombre, email, telefono, fecha_registro, tipo_usuario)
VALUES (1, 'Administrador ChibchaWeb', 'admin@chibchaweb.com', '+57 601 888 0000', NOW(), 'ADMINISTRADOR')
ON CONFLICT (id) DO NOTHING;

INSERT INTO app.usuario (id, nombre, email, telefono, fecha_registro, tipo_usuario)
VALUES
    (2, 'Laura Patricia Díaz Acosta', 'laura.diaz@chibchaweb.com', '+57 310 888 1001', NOW(), 'EMPLEADO'),
    (3, 'Diego Alejandro Rojas Mejía', 'diego.rojas@chibchaweb.com', '+57 310 888 1002', NOW(), 'EMPLEADO')
ON CONFLICT (id) DO NOTHING;

INSERT INTO app.usuario (id, nombre, email, telefono, fecha_registro, tipo_usuario)
VALUES
    (4,  'Carlos Andrés Martínez López',       'carlos.martinez@email.com',    '+57 311 234 5678', NOW(), 'CLIENTE'),
    (5,  'María Fernanda Rincón Torres',       'maria.rincon@email.com',       '+57 320 345 6789', NOW(), 'CLIENTE'),
    (6,  'Juan Pablo Herrera Ospina',          'juan.herrera@email.com',       '+57 300 456 7890', NOW(), 'CLIENTE'),
    (7,  'Laura Valentina Castro Muñoz',       'laura.castro@email.com',       '+57 301 567 8901', NOW(), 'CLIENTE'),
    (8,  'Andrés Felipe Vargas Pérez',         'andres.vargas@email.com',      '+57 310 678 9012', NOW(), 'CLIENTE'),
    (9,  'Daniela Alexandra Rodríguez Silva',  'daniela.rodriguez@email.com',  '+57 315 789 0123', NOW(), 'CLIENTE'),
    (10, 'Sergio Alejandro Mora Jiménez',      'sergio.mora@email.com',        '+57 313 890 1234', NOW(), 'CLIENTE'),
    (11, 'Natalia Andrea Quintero Gil',        'natalia.quintero@email.com',   '+57 317 901 2345', NOW(), 'CLIENTE')
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 3. TABLAS HIJAS JOINED
-- ============================================================
INSERT INTO app.administrador (usuario_id, nivel_acceso)
VALUES (1, 'TOTAL')
ON CONFLICT (usuario_id) DO NOTHING;

INSERT INTO app.empleado (usuario_id, cargo, departamento, salario, fecha_contratacion)
VALUES
    (2, 'Administradora de Sistemas', 'Tecnología', 3500000, '2025-06-01'),
    (3, 'Ejecutivo de Ventas', 'Ventas', 2200000, '2025-08-15')
ON CONFLICT (usuario_id) DO NOTHING;

INSERT INTO app.cliente (usuario_id, documento_identidad, direccion, region)
VALUES
    (4,  'CC 1014567890', 'Calle 72 # 10-34, Chapinero',         'Cundinamarca'),
    (5,  'CC 1128345678', 'Carrera 43 # 22-15, El Poblado',      'Antioquia'),
    (6,  'CC 1094567234', 'Av. 3N # 7-42, San Vicente',          'Valle del Cauca'),
    (7,  'CC 1234567901', 'Calle 84 # 42-18, Riomar',            'Atlántico'),
    (8,  'CC 1073456789', 'Av. San Martín # 12-08, Centro',      'Bolívar'),
    (9,  'CC 1098765432', 'Carrera 33 # 45-12, Cabecera',        'Santander'),
    (10, 'CC 1056789123', 'Calle 15 # 8-36, Circunvalar',        'Risaralda'),
    (11, 'CC 1087654321', 'Carrera 23 # 20-10, El Cable',        'Caldas')
ON CONFLICT (usuario_id) DO NOTHING;

-- ============================================================
-- 4. CREDENCIALES (login)
-- ============================================================
-- admin ya existe (creada por DataInitializer), la omitimos
INSERT INTO app.credencial (id, email, hash_contrasena, intentos_fallidos)
VALUES
    (2,  'laura.diaz@chibchaweb.com',      '$2a$10$8/XhzpR/fakaK2HR7e/LI.lItRj1z4kKpjWWVWujJVxUC8Kt30gl6', 0),
    (3,  'diego.rojas@chibchaweb.com',     '$2a$10$8/XhzpR/fakaK2HR7e/LI.lItRj1z4kKpjWWVWujJVxUC8Kt30gl6', 0),
    (4,  'carlos.martinez@email.com',      '$2a$10$R0fqPCZK7cdviEMGacTsVeFbRqHp1001JE63jSIsal9oPEpvbmCQ6', 0),
    (5,  'maria.rincon@email.com',         '$2a$10$R0fqPCZK7cdviEMGacTsVeFbRqHp1001JE63jSIsal9oPEpvbmCQ6', 0),
    (6,  'juan.herrera@email.com',         '$2a$10$R0fqPCZK7cdviEMGacTsVeFbRqHp1001JE63jSIsal9oPEpvbmCQ6', 0),
    (7,  'laura.castro@email.com',         '$2a$10$R0fqPCZK7cdviEMGacTsVeFbRqHp1001JE63jSIsal9oPEpvbmCQ6', 0),
    (8,  'andres.vargas@email.com',        '$2a$10$R0fqPCZK7cdviEMGacTsVeFbRqHp1001JE63jSIsal9oPEpvbmCQ6', 0),
    (9,  'daniela.rodriguez@email.com',    '$2a$10$R0fqPCZK7cdviEMGacTsVeFbRqHp1001JE63jSIsal9oPEpvbmCQ6', 0),
    (10, 'sergio.mora@email.com',          '$2a$10$R0fqPCZK7cdviEMGacTsVeFbRqHp1001JE63jSIsal9oPEpvbmCQ6', 0),
    (11, 'natalia.quintero@email.com',     '$2a$10$R0fqPCZK7cdviEMGacTsVeFbRqHp1001JE63jSIsal9oPEpvbmCQ6', 0)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 5. CUENTAS DE ACCESO
-- ============================================================
-- admin ya existe (creada por DataInitializer)
-- Roles: 1=ADMINISTRADOR, 2=EMPLEADO, 3=CLIENTE
INSERT INTO app.cuenta_acceso (id, estado, fecha_ultimo_acceso, rol_id, credencial_id)
VALUES
    (2,  'ACTIVA', NOW(), 2, 2),
    (3,  'ACTIVA', NOW(), 2, 3),
    (4,  'ACTIVA', NOW(), 3, 4),
    (5,  'ACTIVA', NOW(), 3, 5),
    (6,  'ACTIVA', NOW(), 3, 6),
    (7,  'ACTIVA', NOW(), 3, 7),
    (8,  'ACTIVA', NOW(), 3, 8),
    (9,  'ACTIVA', NOW(), 3, 9),
    (10, 'ACTIVA', NOW(), 3, 10),
    (11, 'ACTIVA', NOW(), 3, 11)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 6. TARJETAS DE CRÉDITO
-- ============================================================
INSERT INTO app.tarjeta_credito (id, titular, numero, fecha_vencimiento, cvv, tipo_tarjeta, cliente_id)
VALUES
    (1, 'CARLOS ANDRES MARTINEZ LOPEZ',  '4111111111111111', '12/27', '123', 'VISA',       4),
    (2, 'MARIA FERNANDA RINCON TORRES',  '5222222222222222', '06/28', '456', 'MASTERCARD', 5),
    (3, 'JUAN PABLO HERRERA OSPINA',     '4111111111111111', '09/29', '789', 'VISA',       6),
    (4, 'LAURA VALENTINA CASTRO MUÑOZ',  '5222222222222222', '03/27', '234', 'MASTERCARD', 7),
    (5, 'ANDRES FELIPE VARGAS PEREZ',    '3611111111111111', '11/28', '567', 'DINERS',     8),
    (6, 'DANIELA ALEXANDRA RODRIGUEZ S', '4111111111111111', '07/30', '890', 'VISA',       9),
    (7, 'SERGIO ALEJANDRO MORA JIMENEZ', '5222222222222222', '04/29', '111', 'MASTERCARD', 10),
    (8, 'NATALIA ANDREA QUINTERO GIL',   '4111111111111111', '08/28', '222', 'VISA',       11)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 7. PAGOS
-- ============================================================
INSERT INTO app.pago (id, monto, referencia, estado, fecha, cliente_id, periodicidad, tarjeta_id)
VALUES
    (1,  89900,  'VISA-' || EXTRACT(EPOCH FROM NOW())::bigint || '-001', 'APROBADO',  '2026-01-15', 4,  'MENSUAL',     1),
    (2,  49900,  'MC-' || EXTRACT(EPOCH FROM NOW())::bigint || '-002',   'APROBADO',  '2026-02-01', 5,  'MENSUAL',     2),
    (3,  24900,  'VISA-' || EXTRACT(EPOCH FROM NOW())::bigint || '-003', 'APROBADO',  '2026-03-10', 6,  'MENSUAL',     3),
    (4,  179700, 'MC-' || EXTRACT(EPOCH FROM NOW())::bigint || '-004',   'APROBADO',  '2026-01-20', 7,  'TRIMESTRAL',  4),
    (5,  89900,  'DINERS-' || EXTRACT(EPOCH FROM NOW())::bigint || '-005','APROBADO',  '2026-04-05', 8,  'MENSUAL',     5),
    (6,  29900,  'VISA-' || EXTRACT(EPOCH FROM NOW())::bigint || '-006', 'APROBADO',  '2026-02-15', 9,  'MENSUAL',     6),
    (7,  299400, 'MC-' || EXTRACT(EPOCH FROM NOW())::bigint || '-007',   'APROBADO',  '2026-01-01', 10, 'SEMESTRAL',   7),
    (8,  24900,  'VISA-' || EXTRACT(EPOCH FROM NOW())::bigint || '-008', 'PENDIENTE', '2026-05-01', 11, 'MENSUAL',     8),
    (9,  89900,  'VISA-' || EXTRACT(EPOCH FROM NOW())::bigint || '-009', 'APROBADO',  '2026-02-15', 4,  'MENSUAL',     1),
    (10, 89900,  'DINERS-' || EXTRACT(EPOCH FROM NOW())::bigint || '-010','RECHAZADO', '2026-05-10', 8,  'MENSUAL',     5)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 8. SUSCRIPCIONES
-- ============================================================
INSERT INTO app.suscripcion (id, plan_id, cliente_id, periodicidad, fecha_inicio, fecha_fin, estado, pago_id)
VALUES
    (1, 1, 4,  'MENSUAL',    '2026-01-15', '2026-07-15', 'ACTIVA',   1),
    (2, 3, 5,  'MENSUAL',    '2026-02-01', '2026-08-01', 'ACTIVA',   2),
    (3, 5, 6,  'MENSUAL',    '2026-03-10', '2026-09-10', 'ACTIVA',   3),
    (4, 4, 7,  'TRIMESTRAL', '2026-01-20', '2026-07-20', 'ACTIVA',   4),
    (5, 1, 8,  'MENSUAL',    '2026-04-05', '2026-10-05', 'ACTIVA',   5),
    (6, 6, 9,  'MENSUAL',    '2026-02-15', '2026-08-15', 'ACTIVA',   6),
    (7, 3, 10, 'SEMESTRAL',  '2026-01-01', '2026-07-01', 'ACTIVA',   7),
    (8, 5, 11, 'MENSUAL',    '2026-05-01', '2026-11-01', 'ACTIVA',   8)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 9. DOMINIOS
-- ============================================================
INSERT INTO app.dominio (id, nombre, extension, estado, propietario_id, fecha_registro, fecha_vencimiento)
VALUES
    (1,  'martineztech',        '.com.co', 'REGISTRADO',   4, '2026-01-15', '2027-01-15'),
    (2,  'martineztech',        '.com',    'REGISTRADO',   4, '2026-01-15', '2027-01-15'),
    (3,  'rincondigital',       '.com.co', 'REGISTRADO',   5, '2026-02-01', '2027-02-01'),
    (4,  'rincondigital',       '.com',    'REGISTRADO',   5, '2026-02-01', '2027-02-01'),
    (5,  'herrerastudio',       '.com.co', 'REGISTRADO',   6, '2026-03-10', '2027-03-10'),
    (6,  'castroautos',         '.com.co', 'REGISTRADO',   7, '2026-01-20', '2027-01-20'),
    (7,  'vargasviajes',        '.com.co', 'REGISTRADO',   8, '2026-04-05', '2027-04-05'),
    (8,  'rodriguezconstruye',  '.com.co', 'REGISTRADO',   9, '2026-02-15', '2027-02-15'),
    (9,  'moraecolodge',        '.com.co', 'REGISTRADO',   10, '2026-01-01', '2027-01-01'),
    (10, 'quinteroinnovaciones', '.com.co', 'REGISTRADO',  11, '2026-05-01', '2027-05-01')
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 10. SITIOS WEB
-- ============================================================
INSERT INTO app.sitio_web (id, url_sitio, espacio_usado, estado_activo, fecha_creacion, propietario_id, dominio_id, suscripcion_id)
VALUES
    (1, 'martineztech.com.co',       512,   true, '2026-01-20', 4,  1,  1),
    (2, 'martineztech.com',          204,   true, '2026-01-20', 4,  2,  1),
    (3, 'rincondigital.com.co',      1280,  true, '2026-02-05', 5,  3,  2),
    (4, 'herrerastudio.com.co',      350,   true, '2026-03-15', 6,  5,  3),
    (5, 'castroautos.com.co',        850,   true, '2026-01-25', 7,  6,  4),
    (6, 'vargasviajes.com.co',       180,   true, '2026-04-10', 8,  7,  5),
    (7, 'rodriguezconstruye.com.co',  2150,  true, '2026-02-20', 9,  8,  6),
    (8, 'moraecolodge.com.co',       920,   true, '2026-01-05', 10, 9,  7),
    (9, 'quinteroinnovaciones.com.co',120,   true, '2026-05-05', 11, 10, 8)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 11. TICKETS DE SOPORTE
-- ============================================================
INSERT INTO app.ticket (id, titulo, descripcion, nivel, prioridad, estado, cliente_id, fecha_creacion, fecha_resolucion)
VALUES
    (1, 'Problema con el correo corporativo',
     'No puedo enviar ni recibir correos desde mi cuenta admin@rincondigital.com.co desde hace 2 horas. Ya intenté cambiar la contraseña pero sigue igual.',
     2, 'ALTA', 'EN_PROCESO', 5, '2026-05-20 09:30:00', NULL),
    (2, 'Migración de sitio web lenta',
     'Estamos migrando castroautos.com.co desde otro proveedor y la transferencia de archivos es extremadamente lenta (menos de 1 MB/s).',
     1, 'MEDIA', 'ABIERTO', 7, '2026-05-22 14:15:00', NULL),
    (3, 'No puedo acceder al panel de control',
     'Desde ayer no puedo iniciar sesión en el panel de administración de mi sitio. Me aparece "Credenciales inválidas" aunque uso las mismas de siempre.',
     3, 'CRITICA', 'RESUELTO', 10, '2026-05-18 08:00:00', '2026-05-18 10:30:00'),
    (4, 'Renovación de dominio no aplicada',
     'Renové el dominio martineztech.com.co hace 3 días pero el pago sigue apareciendo como pendiente.',
     1, 'BAJA', 'ABIERTO', 4, '2026-05-25 11:00:00', NULL)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 12. DISTRIBUIDORES
-- ============================================================
INSERT INTO app.distribuidor (id, nombre, email, region, codigo_distribuidor, max_dominios, nivel_distribuidor)
VALUES
    (1, 'Distribuidora Tecnológica Colombia SAS',  'contacto@distecnologica.com.co', 'Bogotá',   'DIST-1', 50,  'BASICO'),
    (2, 'Hosting Solutions Centroamérica SAS',       'ventas@hostingsolutions.co',    'Medellín', 'DIST-2', 500, 'PREMIUM'),
    (3, 'Web Services del Pacífico LTDA',           'info@webservicespacifico.co',   'Cali',     'DIST-3', 150, 'PREMIUM')
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 13. RESETEAR SECUENCIAS AUTO-INCREMENTALES
-- ============================================================
SELECT setval('app.hosting_plan_id_seq',       COALESCE((SELECT MAX(id) FROM app.hosting_plan), 0));
SELECT setval('app.usuario_id_seq',            COALESCE((SELECT MAX(id) FROM app.usuario), 0));
SELECT setval('app.credencial_id_seq',         COALESCE((SELECT MAX(id) FROM app.credencial), 0));
SELECT setval('app.cuenta_acceso_id_seq',      COALESCE((SELECT MAX(id) FROM app.cuenta_acceso), 0));
SELECT setval('app.tarjeta_credito_id_seq',    COALESCE((SELECT MAX(id) FROM app.tarjeta_credito), 0));
SELECT setval('app.pago_id_seq',               COALESCE((SELECT MAX(id) FROM app.pago), 0));
SELECT setval('app.suscripcion_id_seq',        COALESCE((SELECT MAX(id) FROM app.suscripcion), 0));
SELECT setval('app.dominio_id_seq',            COALESCE((SELECT MAX(id) FROM app.dominio), 0));
SELECT setval('app.sitio_web_id_seq',          COALESCE((SELECT MAX(id) FROM app.sitio_web), 0));
SELECT setval('app.ticket_id_seq',             COALESCE((SELECT MAX(id) FROM app.ticket), 0));
SELECT setval('app.distribuidor_id_seq',       COALESCE((SELECT MAX(id) FROM app.distribuidor), 0));

COMMIT;
