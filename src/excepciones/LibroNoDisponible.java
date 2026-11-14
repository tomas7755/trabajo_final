package excepciones;

/**
 * Se lanza cuando se quiere prestar o reservar un libro que no esta disponible para esa operacion
 * (ya prestado, ya reservado por otro usuario, etc.).
 * Extiende de RuntimeException (excepcion no chequeada).
 */
public class LibroNoDisponible extends RuntimeException {
    public LibroNoDisponible(String message) {
        super(message);
    }
}
