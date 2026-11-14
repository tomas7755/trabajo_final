package interfaces;

/**
 * Interfaz para todo lo que puede iniciar sesion en el sistema.
 * Quien la implemente esta obligado a definir como se valida su contraseña.
 */
public interface Autenticable {

    //devuelve true si la contraseña es correcta y la cuenta esta activa.
    boolean autenticar(String contrasenia);
}