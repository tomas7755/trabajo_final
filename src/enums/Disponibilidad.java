package enums;

/**
 * Estados posibles de un libro segun donde esta fisicamente.
 */
public enum Disponibilidad {
    //esta en la biblioteca y se puede pedir prestado.
    DISPONIBLE,
    //lo tiene un usuario.
    PRESTADO,
    //fue devuelto y esta guardado para el usuario que lo reservo.
    RESERVADO
}
