package modelo.personas;

/**
 * Representa al administrador del sistema.
 * Es el encargado de las funciones administrativas: crear otros usuarios y bibliotecarios,
 * darlos de baja, asignar permisos y mantener el catalogo actualizado.
 * Hereda de Persona todos sus datos (dni, nombre, apellido, email, contrasenia, activo).
 */
public class Administrador extends Persona {

    //constructores:
    public Administrador() {
        super();
    }
    public Administrador(String dni, String nombre, String apellido, String email, String contrasenia) {
        //super llama al constructor de Persona para cargar los datos.
        super(dni, nombre, apellido, email, contrasenia);
    }

    //metodos:
    //implementa el metodo abstracto de Persona.
    @Override
    public String getTipo() {
        return "Administrador";
    }
}