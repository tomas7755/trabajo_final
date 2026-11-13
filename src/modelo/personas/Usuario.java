package modelo.personas;

/**
 * Representa a un usuario de la biblioteca (la persona que pide libros prestados).
 * Hereda de Persona los datos comunes (dni, nombre, apellido, email, contrasenia, activo)
 * y agrega los datos de contacto propios de un usuario.
 */

public class Usuario extends Persona {
    private String direccion;
    private String telefono;

    //constructores:
    public Usuario() {
        super();
    }
    public Usuario(String dni, String nombre, String apellido, String email, String contrasenia,
                   String direccion, String telefono) {
        //super llama al constructor de Persona para cargar los datos comunes.
        super(dni, nombre, apellido, email, contrasenia);
        this.direccion = direccion;
        this.telefono = telefono;
    }

    //getters:
    public String getDireccion() {
        return direccion;
    }
    public String getTelefono() {
        return telefono;
    }

    //setters:
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    //metodos:
    //implementa el metodo abstracto de Persona.
    @Override
    public String getTipo() {
        return "Usuario";
    }
    //reutiliza el toString de Persona y le agrega los datos de contacto.
    @Override
    public String toString() {
        return super.toString() +
                ", Direccion: " + direccion +
                ", Telefono: " + telefono;
    }
}