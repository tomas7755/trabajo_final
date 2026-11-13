package excepciones;

/**
 * Se lanza cuando se busca una persona (usuario, bibliotecario o administrador)
 * que no esta registrada en el sistema.
 */
public class PersonaNoEncontradaException extends RuntimeException {
    public PersonaNoEncontradaException(String message) {
        super(message);
    }
}