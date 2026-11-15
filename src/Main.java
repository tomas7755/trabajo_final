import vista.Gestion;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Application.launch(Gestion.class, args);
    }
}

/**
 * CREATE DATABASE IF NOT EXISTS biblioteca;
 * USE biblioteca;
 *
 * CREATE TABLE persona (
 *     dni         VARCHAR(20) PRIMARY KEY,
 *     tipo        ENUM('USUARIO','BIBLIOTECARIO','ADMINISTRADOR') NOT NULL,
 *     nombre      VARCHAR(60) NOT NULL,
 *     apellido    VARCHAR(60) NOT NULL,
 *     email       VARCHAR(100) NOT NULL,
 *     contrasenia VARCHAR(100) NOT NULL,
 *     activo      BOOLEAN NOT NULL DEFAULT TRUE,
 *     legajo      VARCHAR(30) NULL,      -- solo bibliotecarios
 *     direccion   VARCHAR(120) NULL,     -- solo usuarios
 *     telefono    VARCHAR(30) NULL       -- solo usuarios
 * );
 *
 * CREATE TABLE libro (
 *     numero_identificador INT PRIMARY KEY,
 *     nombre         VARCHAR(150) NOT NULL,
 *     autor          VARCHAR(100) NOT NULL,
 *     genero         VARCHAR(60)  NOT NULL,
 *     disponibilidad ENUM('DISPONIBLE','PRESTADO','RESERVADO') NOT NULL,
 *     activo         BOOLEAN NOT NULL DEFAULT TRUE
 * );
 *
 * CREATE TABLE prestamo (
 *     numero_prestamo  INT PRIMARY KEY,
 *     dni_usuario      VARCHAR(20) NOT NULL,
 *     numero_libro     INT NOT NULL,
 *     fecha_prestamo   DATE NOT NULL,
 *     fecha_devolucion DATE NOT NULL,
 *     FOREIGN KEY (dni_usuario) REFERENCES persona(dni),
 *     FOREIGN KEY (numero_libro) REFERENCES libro(numero_identificador)
 * );
 *
 * CREATE TABLE reserva (
 *     numero_reserva INT PRIMARY KEY,
 *     dni_usuario    VARCHAR(20) NOT NULL,
 *     numero_libro   INT NOT NULL,
 *     FOREIGN KEY (dni_usuario) REFERENCES persona(dni),
 *     FOREIGN KEY (numero_libro) REFERENCES libro(numero_identificador)
 * );
 *
 *
 * USE biblioteca;
 * SET SQL_SAFE_UPDATES = 0;
 * DELETE FROM reserva;
 * DELETE FROM prestamo;
 * DELETE FROM libro;
 * DELETE FROM persona;
 *
 * SET SQL_SAFE_UPDATES = 1;
 *
 * -- PERSONAL (administradores y bibliotecarios)
 * INSERT INTO persona (dni, tipo, nombre, apellido, email, contrasenia, activo, legajo, direccion, telefono) VALUES
 * ('20111222', 'ADMINISTRADOR', 'Laura',    'Medina',  'laura.medina@biblioteca.com','1234', TRUE,  NULL,    NULL, NULL),
 * ('22222222', 'BIBLIOTECARIO', 'Biblio',   'Básico',  'biblio@biblioteca.com',      '1234', TRUE,  'L-001', NULL, NULL),
 * ('27111222', 'BIBLIOTECARIO', 'Paula',    'Benítez', 'paula.benitez@biblioteca.com','1234', TRUE, 'L-002', NULL, NULL),
 * ('27222333', 'BIBLIOTECARIO', 'Ricardo',  'Vega',    'ricardo.vega@biblioteca.com','1234', FALSE, 'L-003', NULL, NULL);
 *
 *
 * -- USUARIOS
 * INSERT INTO persona (dni, tipo, nombre, apellido, email, contrasenia, activo, legajo, direccion, telefono) VALUES
 * ('33333333', 'USUARIO', 'Usuario',    'Básico',    'usuario@mail.com',           '1234', TRUE,  NULL, 'Calle Falsa 123',        '2234001111'),
 * ('30111222', 'USUARIO', 'Lucía',      'Fernández', 'lucia.fernandez@mail.com',   '1234', TRUE,  NULL, 'Colón 1520',             '2234002222'),
 * ('30222333', 'USUARIO', 'Martín',     'Gómez',     'martin.gomez@mail.com',      '1234', TRUE,  NULL, 'Güemes 2840',            '2234003333'),
 * ('30333444', 'USUARIO', 'Sofía',      'Rodríguez', 'sofia.rodriguez@mail.com',   '1234', TRUE,  NULL, 'Independencia 960',      '2234004444'),
 * ('30444555', 'USUARIO', 'Julián',     'Pérez',     'julian.perez@mail.com',      '1234', TRUE,  NULL, 'Rivadavia 2105',         '2234005555'),
 * ('30555666', 'USUARIO', 'Camila',     'López',     'camila.lopez@mail.com',      '1234', TRUE,  NULL, 'Alberti 740',            '2234006666'),
 * ('30666777', 'USUARIO', 'Mateo',      'Díaz',      'mateo.diaz@mail.com',        '1234', TRUE,  NULL, 'Luro 3310',              '2234007777'),
 * ('30777888', 'USUARIO', 'Valentina',  'Sosa',      'valentina.sosa@mail.com',    '1234', TRUE,  NULL, 'San Martín 1875',        '2234008888'),
 * ('30888999', 'USUARIO', 'Tomás',      'Álvarez',   'tomas.alvarez@mail.com',     '1234', TRUE,  NULL, 'Mitre 450',              '2234009999'),
 * ('30999000', 'USUARIO', 'Florencia',  'Romero',    'florencia.romero@mail.com',  '1234', TRUE,  NULL, 'Belgrano 1290',          '2234010000'),
 * ('31111222', 'USUARIO', 'Nicolás',    'Castro',    'nicolas.castro@mail.com',    '1234', TRUE,  NULL, 'Falucho 2250',           '2234011111'),
 * ('31222333', 'USUARIO', 'Agustina',   'Herrera',   'agustina.herrera@mail.com',  '1234', TRUE,  NULL, 'Moreno 3070',            '2234012222'),
 * ('31333444', 'USUARIO', 'Bruno',      'Molina',    'bruno.molina@mail.com',      '1234', TRUE,  NULL, 'Almafuerte 615',         '2234013333'),
 * ('31444555', 'USUARIO', 'Carolina',   'Ruiz',      'carolina.ruiz@mail.com',     '1234', FALSE, NULL, 'Peña 1980',              '2234014444'),
 * ('31555666', 'USUARIO', 'Federico',   'Silva',     'federico.silva@mail.com',    '1234', TRUE,  NULL, 'Catamarca 2400',         '2234015555');
 *
 * -- LIBROS
 * INSERT INTO libro (numero_identificador, nombre, autor, genero, disponibilidad, activo) VALUES
 * (1,  'Cien años de soledad',                          'Gabriel García Márquez', 'Novela',          'PRESTADO',   TRUE),
 * (2,  'El Aleph',                                      'Jorge Luis Borges',      'Cuento',          'DISPONIBLE', TRUE),
 * (3,  'Rayuela',                                       'Julio Cortázar',         'Novela',          'PRESTADO',   TRUE),
 * (4,  'Ficciones',                                     'Jorge Luis Borges',      'Cuento',          'DISPONIBLE', TRUE),
 * (5,  'Veinte poemas de amor y una canción desesperada','Pablo Neruda',          'Poesía',          'RESERVADO',  TRUE),
 * (6,  'Martín Fierro',                                 'José Hernández',         'Poesía',          'PRESTADO',   TRUE),
 * (7,  'Sobre héroes y tumbas',                         'Ernesto Sabato',         'Novela',          'PRESTADO',   TRUE),
 * (8,  'El túnel',                                      'Ernesto Sabato',         'Novela',          'DISPONIBLE', TRUE),
 * (9,  'Pedro Páramo',                                  'Juan Rulfo',             'Novela',          'PRESTADO',   TRUE),
 * (10, 'Crónica de una muerte anunciada',               'Gabriel García Márquez', 'Novela',          'DISPONIBLE', TRUE),
 * (11, 'Dune',                                          'Frank Herbert',          'Ciencia ficción', 'PRESTADO',   TRUE),
 * (12, 'Fundación',                                     'Isaac Asimov',           'Ciencia ficción', 'DISPONIBLE', TRUE),
 * (13, 'Harry Potter y la piedra filosofal',            'J. K. Rowling',          'Fantasía',        'RESERVADO',  TRUE),
 * (14, 'Historia de la eternidad',                      'Jorge Luis Borges',      'Ensayo',          'DISPONIBLE', FALSE),
 * (15, 'Facundo',                                       'Domingo F. Sarmiento',   'Ensayo',          'DISPONIBLE', TRUE);
 *
 *
 * -- PRESTAMOS
 * INSERT INTO prestamo (numero_prestamo, dni_usuario, numero_libro, fecha_prestamo, fecha_devolucion) VALUES
 * (1, '33333333', 1,  '2026-11-10', '2026-11-25'),
 * (2, '30111222', 3,  '2026-11-12', '2026-11-27'),
 * (3, '30333444', 7,  '2026-11-01', '2026-12-01'),
 * (4, '30444555', 9,  '2026-11-05', '2026-11-20'),
 * (5, '30666777', 11, '2026-11-14', '2026-11-29'),
 * (6, '30777888', 6,  '2026-10-20', '2026-11-04');
 *
 *
 * -- RESERVAS
 * INSERT INTO reserva (numero_reserva, dni_usuario, numero_libro) VALUES
 * (1, '30222333', 3),
 * (2, '30888999', 5),
 * (3, '30555666', 9),
 * (4, '30999000', 13);
 */