package excepciones;

public class SinUsuarioException extends RuntimeException {
    public SinUsuarioException(String message) {
        super(message);
    }
}
