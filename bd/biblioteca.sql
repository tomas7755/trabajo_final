-- =====================================================================
-- Base de datos del Sistema de Biblioteca (Programacion II - TP Final)
-- Crea la base desde cero y carga 15 datos de cada tipo.
-- ATENCION: borra la base "biblioteca" si ya existia (se pierden los cambios hechos desde el programa).
-- Como usarlo: abrir este archivo en MySQL Workbench y ejecutarlo completo (el rayo).
-- =====================================================================

DROP DATABASE IF EXISTS biblioteca;
CREATE DATABASE biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE biblioteca;
SET NAMES utf8mb4;

-- Usuarios, bibliotecarios y administradores van en la misma tabla.
-- La columna "tipo" dice que clase hija es cada fila (herencia de Persona).
CREATE TABLE persona (
    dni         VARCHAR(20) PRIMARY KEY,
    tipo        ENUM('USUARIO','BIBLIOTECARIO','ADMINISTRADOR') NOT NULL,
    nombre      VARCHAR(60) NOT NULL,
    apellido    VARCHAR(60) NOT NULL,
    email       VARCHAR(100) NOT NULL,
    contrasenia VARCHAR(100) NOT NULL,
    activo      BOOLEAN NOT NULL DEFAULT TRUE,   -- baja logica
    legajo      VARCHAR(30) NULL,                -- solo bibliotecarios
    direccion   VARCHAR(120) NULL,               -- solo usuarios
    telefono    VARCHAR(30) NULL                 -- solo usuarios
);

CREATE TABLE libro (
    numero_identificador INT PRIMARY KEY,
    nombre         VARCHAR(150) NOT NULL,
    autor          VARCHAR(100) NOT NULL,
    genero         VARCHAR(60)  NOT NULL,
    disponibilidad ENUM('DISPONIBLE','PRESTADO','RESERVADO') NOT NULL,
    activo         BOOLEAN NOT NULL DEFAULT TRUE  -- baja logica
);

CREATE TABLE prestamo (
    numero_prestamo  INT PRIMARY KEY,
    dni_usuario      VARCHAR(20) NOT NULL,
    numero_libro     INT NOT NULL,
    fecha_prestamo   DATE NOT NULL,
    fecha_devolucion DATE NOT NULL,
    FOREIGN KEY (dni_usuario) REFERENCES persona(dni),
    FOREIGN KEY (numero_libro) REFERENCES libro(numero_identificador)
);

CREATE TABLE reserva (
    numero_reserva INT PRIMARY KEY,
    dni_usuario    VARCHAR(20) NOT NULL,
    numero_libro   INT NOT NULL,
    FOREIGN KEY (dni_usuario) REFERENCES persona(dni),
    FOREIGN KEY (numero_libro) REFERENCES libro(numero_identificador)
);

-- ---------------------------------------------------------------------
-- PERSONAL: 2 administradores y 13 bibliotecarios (contraseña de todos: 1234)
-- ---------------------------------------------------------------------
INSERT INTO persona (dni, tipo, nombre, apellido, email, contrasenia, activo, legajo, direccion, telefono) VALUES
('20111222', 'ADMINISTRADOR', 'Laura',     'Medina',    'laura.medina@biblioteca.com',     '1234', TRUE,  NULL,    NULL, NULL),
('20333444', 'ADMINISTRADOR', 'Hernán',    'Ortiz',     'hernan.ortiz@biblioteca.com',     '1234', TRUE,  NULL,    NULL, NULL),
('22222222', 'BIBLIOTECARIO', 'Biblio',    'Básico',    'biblio@biblioteca.com',           '1234', TRUE,  'L-001', NULL, NULL),
('27111222', 'BIBLIOTECARIO', 'Paula',     'Benítez',   'paula.benitez@biblioteca.com',    '1234', TRUE,  'L-002', NULL, NULL),
('27222333', 'BIBLIOTECARIO', 'Ricardo',   'Vega',      'ricardo.vega@biblioteca.com',     '1234', FALSE, 'L-003', NULL, NULL),
('27333444', 'BIBLIOTECARIO', 'Mariana',   'Quiroga',   'mariana.quiroga@biblioteca.com',  '1234', TRUE,  'L-004', NULL, NULL),
('27444555', 'BIBLIOTECARIO', 'Gustavo',   'Ibarra',    'gustavo.ibarra@biblioteca.com',   '1234', TRUE,  'L-005', NULL, NULL),
('27555666', 'BIBLIOTECARIO', 'Silvina',   'Acosta',    'silvina.acosta@biblioteca.com',   '1234', TRUE,  'L-006', NULL, NULL),
('27666777', 'BIBLIOTECARIO', 'Diego',     'Paz',       'diego.paz@biblioteca.com',        '1234', TRUE,  'L-007', NULL, NULL),
('27777888', 'BIBLIOTECARIO', 'Andrea',    'Luna',      'andrea.luna@biblioteca.com',      '1234', TRUE,  'L-008', NULL, NULL),
('27888999', 'BIBLIOTECARIO', 'Pablo',     'Rossi',     'pablo.rossi@biblioteca.com',      '1234', TRUE,  'L-009', NULL, NULL),
('27999000', 'BIBLIOTECARIO', 'Natalia',   'Ferreyra',  'natalia.ferreyra@biblioteca.com', '1234', FALSE, 'L-010', NULL, NULL),
('28111222', 'BIBLIOTECARIO', 'Ezequiel',  'Navarro',   'ezequiel.navarro@biblioteca.com', '1234', TRUE,  'L-011', NULL, NULL),
('28222333', 'BIBLIOTECARIO', 'Verónica',  'Giménez',   'veronica.gimenez@biblioteca.com', '1234', TRUE,  'L-012', NULL, NULL),
('28333444', 'BIBLIOTECARIO', 'Leandro',   'Cabrera',   'leandro.cabrera@biblioteca.com',  '1234', TRUE,  'L-013', NULL, NULL);

-- ---------------------------------------------------------------------
-- USUARIOS (contraseña de todos: 1234). Carolina Ruiz esta dada de baja.
-- ---------------------------------------------------------------------
INSERT INTO persona (dni, tipo, nombre, apellido, email, contrasenia, activo, legajo, direccion, telefono) VALUES
('33333333', 'USUARIO', 'Usuario',   'Básico',    'usuario@mail.com',          '1234', TRUE,  NULL, 'Calle Falsa 123',   '2234001111'),
('30111222', 'USUARIO', 'Lucía',     'Fernández', 'lucia.fernandez@mail.com',  '1234', TRUE,  NULL, 'Colón 1520',        '2234002222'),
('30222333', 'USUARIO', 'Martín',    'Gómez',     'martin.gomez@mail.com',     '1234', TRUE,  NULL, 'Güemes 2840',       '2234003333'),
('30333444', 'USUARIO', 'Sofía',     'Rodríguez', 'sofia.rodriguez@mail.com',  '1234', TRUE,  NULL, 'Independencia 960', '2234004444'),
('30444555', 'USUARIO', 'Julián',    'Pérez',     'julian.perez@mail.com',     '1234', TRUE,  NULL, 'Rivadavia 2105',    '2234005555'),
('30555666', 'USUARIO', 'Camila',    'López',     'camila.lopez@mail.com',     '1234', TRUE,  NULL, 'Alberti 740',       '2234006666'),
('30666777', 'USUARIO', 'Mateo',     'Díaz',      'mateo.diaz@mail.com',       '1234', TRUE,  NULL, 'Luro 3310',         '2234007777'),
('30777888', 'USUARIO', 'Valentina', 'Sosa',      'valentina.sosa@mail.com',   '1234', TRUE,  NULL, 'San Martín 1875',   '2234008888'),
('30888999', 'USUARIO', 'Tomás',     'Álvarez',   'tomas.alvarez@mail.com',    '1234', TRUE,  NULL, 'Mitre 450',         '2234009999'),
('30999000', 'USUARIO', 'Florencia', 'Romero',    'florencia.romero@mail.com', '1234', TRUE,  NULL, 'Belgrano 1290',     '2234010000'),
('31111222', 'USUARIO', 'Nicolás',   'Castro',    'nicolas.castro@mail.com',   '1234', TRUE,  NULL, 'Falucho 2250',      '2234011111'),
('31222333', 'USUARIO', 'Agustina',  'Herrera',   'agustina.herrera@mail.com', '1234', TRUE,  NULL, 'Moreno 3070',       '2234012222'),
('31333444', 'USUARIO', 'Bruno',     'Molina',    'bruno.molina@mail.com',     '1234', TRUE,  NULL, 'Almafuerte 615',    '2234013333'),
('31444555', 'USUARIO', 'Carolina',  'Ruiz',      'carolina.ruiz@mail.com',    '1234', FALSE, NULL, 'Peña 1980',         '2234014444'),
('31555666', 'USUARIO', 'Federico',  'Silva',     'federico.silva@mail.com',   '1234', TRUE,  NULL, 'Catamarca 2400',    '2234015555');

-- ---------------------------------------------------------------------
-- LIBROS (25). El estado tiene que coincidir con los prestamos y reservas de abajo:
--   PRESTADO   = tiene un prestamo (puede tener ademas una reserva esperando)
--   RESERVADO  = no esta prestado, pero esta guardado para quien lo reservo
--   DISPONIBLE = sin prestamo ni reserva
-- El libro 14 esta dado de baja (baja logica).
-- ---------------------------------------------------------------------
INSERT INTO libro (numero_identificador, nombre, autor, genero, disponibilidad, activo) VALUES
(1,  'Cien años de soledad',                           'Gabriel García Márquez', 'Novela',          'PRESTADO',   TRUE),
(2,  'El Aleph',                                       'Jorge Luis Borges',      'Cuento',          'RESERVADO',  TRUE),
(3,  'Rayuela',                                        'Julio Cortázar',         'Novela',          'PRESTADO',   TRUE),
(4,  'Ficciones',                                      'Jorge Luis Borges',      'Cuento',          'DISPONIBLE', TRUE),
(5,  'Veinte poemas de amor y una canción desesperada','Pablo Neruda',           'Poesía',          'RESERVADO',  TRUE),
(6,  'Martín Fierro',                                  'José Hernández',         'Poesía',          'PRESTADO',   TRUE),
(7,  'Sobre héroes y tumbas',                          'Ernesto Sabato',         'Novela',          'PRESTADO',   TRUE),
(8,  'El túnel',                                       'Ernesto Sabato',         'Novela',          'DISPONIBLE', TRUE),
(9,  'Pedro Páramo',                                   'Juan Rulfo',             'Novela',          'PRESTADO',   TRUE),
(10, 'Crónica de una muerte anunciada',                'Gabriel García Márquez', 'Novela',          'DISPONIBLE', TRUE),
(11, 'Dune',                                           'Frank Herbert',          'Ciencia ficción', 'PRESTADO',   TRUE),
(12, 'Fundación',                                      'Isaac Asimov',           'Ciencia ficción', 'RESERVADO',  TRUE),
(13, 'Harry Potter y la piedra filosofal',             'J. K. Rowling',          'Fantasía',        'RESERVADO',  TRUE),
(14, 'Historia de la eternidad',                       'Jorge Luis Borges',      'Ensayo',          'DISPONIBLE', FALSE),
(15, 'Facundo',                                        'Domingo F. Sarmiento',   'Ensayo',          'DISPONIBLE', TRUE),
(16, 'La invención de Morel',                          'Adolfo Bioy Casares',    'Novela',          'PRESTADO',   TRUE),
(17, 'Bestiario',                                      'Julio Cortázar',         'Cuento',          'PRESTADO',   TRUE),
(18, 'El juguete rabioso',                             'Roberto Arlt',           'Novela',          'PRESTADO',   TRUE),
(19, 'Los siete locos',                                'Roberto Arlt',           'Novela',          'PRESTADO',   TRUE),
(20, 'Ensayo sobre la ceguera',                        'José Saramago',          'Novela',          'PRESTADO',   TRUE),
(21, 'Yo, robot',                                      'Isaac Asimov',           'Ciencia ficción', 'PRESTADO',   TRUE),
(22, 'El señor de los anillos',                        'J. R. R. Tolkien',       'Fantasía',        'PRESTADO',   TRUE),
(23, '1984',                                           'George Orwell',          'Novela',          'PRESTADO',   TRUE),
(24, 'Fahrenheit 451',                                 'Ray Bradbury',           'Ciencia ficción', 'PRESTADO',   TRUE),
(25, 'Las venas abiertas de América Latina',           'Eduardo Galeano',        'Ensayo',          'DISPONIBLE', TRUE);

-- ---------------------------------------------------------------------
-- PRESTAMOS (15): uno por cada libro PRESTADO. Algunos ya estan vencidos.
-- ---------------------------------------------------------------------
INSERT INTO prestamo (numero_prestamo, dni_usuario, numero_libro, fecha_prestamo, fecha_devolucion) VALUES
(1,  '33333333', 1,  '2026-11-10', '2026-11-25'),
(2,  '30111222', 3,  '2026-11-12', '2026-11-27'),
(3,  '30333444', 7,  '2026-11-01', '2026-12-01'),
(4,  '30444555', 9,  '2026-11-05', '2026-11-20'),
(5,  '30666777', 11, '2026-11-14', '2026-11-29'),
(6,  '30777888', 6,  '2026-10-20', '2026-11-04'),
(7,  '30222333', 16, '2026-11-08', '2026-11-23'),
(8,  '30555666', 17, '2026-11-09', '2026-11-24'),
(9,  '30888999', 18, '2026-11-11', '2026-11-26'),
(10, '30999000', 19, '2026-11-02', '2026-11-17'),
(11, '31111222', 20, '2026-11-13', '2026-11-28'),
(12, '31222333', 21, '2026-11-06', '2026-11-21'),
(13, '31333444', 22, '2026-11-14', '2026-11-29'),
(14, '31555666', 23, '2026-11-03', '2026-11-18'),
(15, '30111222', 24, '2026-11-15', '2026-11-30');

-- ---------------------------------------------------------------------
-- RESERVAS (15): una sola por libro, y nunca de la misma persona que lo tiene prestado.
-- 11 son de libros PRESTADOS (esperan que se devuelvan) y 4 de libros RESERVADOS (listos para retirar).
-- ---------------------------------------------------------------------
INSERT INTO reserva (numero_reserva, dni_usuario, numero_libro) VALUES
(1,  '30222333', 3),
(2,  '30888999', 5),
(3,  '30555666', 9),
(4,  '30999000', 13),
(5,  '30333444', 1),
(6,  '31111222', 6),
(7,  '30444555', 7),
(8,  '31222333', 11),
(9,  '33333333', 16),
(10, '30666777', 17),
(11, '30777888', 18),
(12, '31333444', 19),
(13, '31555666', 20),
(14, '30222333', 2),
(15, '30555666', 12);
