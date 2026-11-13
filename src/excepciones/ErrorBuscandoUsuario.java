package excepciones;

public class ErrorBuscandoUsuario extends RuntimeException {
    public ErrorBuscandoUsuario(String message) {
        super(message);
    }
}
