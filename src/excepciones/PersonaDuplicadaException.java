package excepciones;

/**
 * Se lanza cuando se intenta registrar una persona con un dni o un email
 * que ya pertenece a otra persona del sistema.
 */
public class PersonaDuplicadaException extends RuntimeException {
    public PersonaDuplicadaException(String message) {
        super(message);
    }
}