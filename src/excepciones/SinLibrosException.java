package excepciones;

/**
 * Se lanza cuando se quiere prestar o reservar un libro que no existe o fue dado de baja.
 * Extiende de RuntimeException (excepcion no chequeada).
 */
public class SinLibrosException extends RuntimeException {
    public SinLibrosException(String message) {
        super(message);
    }
}
