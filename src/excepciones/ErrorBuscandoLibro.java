package excepciones;

public class ErrorBuscandoLibro extends RuntimeException {
  public ErrorBuscandoLibro(String message) {
    super(message);
  }
}
