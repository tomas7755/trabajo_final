package excepciones;

/**
 * Se lanza cuando se quiere hacer un prestamo o reserva para un usuario que no existe o esta dado de baja.
 * Extiende de RuntimeException (excepcion no chequeada).
 */
public class SinUsuarioException extends RuntimeException {
    public SinUsuarioException(String message) {
        super(message);
    }
}
