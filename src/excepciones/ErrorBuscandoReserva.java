package excepciones;

public class ErrorBuscandoReserva extends RuntimeException {
    public ErrorBuscandoReserva(String message) {
        super(message);
    }
}
