package excepciones;

/**
 * Se lanza cuando se busca, modifica, renueva o devuelve un prestamo que no existe.
 * Extiende de RuntimeException (excepcion no chequeada).
 */
public class ErrorBuscandoPrestamo extends RuntimeException {
    public ErrorBuscandoPrestamo(String message) {
        super(message);
    }
}
