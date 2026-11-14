package excepciones;

/**
 * Se lanza cuando se busca, modifica, da de baja o reactiva un usuario que no existe
 * o que no se puede dar de baja (por tener prestamos o reservas).
 * Extiende de RuntimeException (excepcion no chequeada).
 */
public class ErrorBuscandoUsuario extends RuntimeException {
    public ErrorBuscandoUsuario(String message) {
        super(message);
    }
}
