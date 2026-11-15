package excepciones;

/**
 * Se lanza cuando falla algo con la base de datos (no hay conexion, error en un INSERT, UPDATE, etc.).
 * Envuelve la SQLException para que las demas capas no dependan de JDBC.
 */
public class ErrorEnBasesDeDatosException extends RuntimeException {
    public ErrorEnBasesDeDatosException(String message) {
        super(message);
    }
}
