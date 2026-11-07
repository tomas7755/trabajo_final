package Excepciones;

public class ErrorBuscandoPrestamo extends RuntimeException {
    public ErrorBuscandoPrestamo(String message) {
        super(message);
    }
}
