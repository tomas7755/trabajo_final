package excepciones;

/**
 * Se lanza cuando se busca, modifica o cancela una reserva que no existe.
 * Extiende de RuntimeException (excepcion no chequeada).
 */
public class ErrorBuscandoReserva extends RuntimeException {
    public ErrorBuscandoReserva(String message) {
        super(message);
    }
}
