package excepciones;

/**
 * Se lanza cuando se busca, modifica o elimina un libro que no existe o cuyos datos son invalidos.
 * Extiende de RuntimeException (excepcion no chequeada).
 */
public class ErrorBuscandoLibro extends RuntimeException {
  public ErrorBuscandoLibro(String message) {
    super(message);
  }
}
