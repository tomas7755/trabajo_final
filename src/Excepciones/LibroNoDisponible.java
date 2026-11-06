package Excepciones;
public class LibroNoDisponible extends RuntimeException {
    public LibroNoDisponible(String message) {
        super(message);
    }
}
