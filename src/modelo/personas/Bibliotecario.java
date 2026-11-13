package modelo.personas;

/**
 * Representa a un bibliotecario: el empleado que atiende a los usuarios,
 * registra prestamos, devoluciones y reservas.
 * Hereda de Persona los datos comunes y agrega su numero de legajo.
 */
public class Bibliotecario extends Persona {
    //numero de empleado que identifica al bibliotecario dentro de la biblioteca.
    private String legajo;

    //constructores:
    public Bibliotecario() {
        super();
    }
    public Bibliotecario(String dni, String nombre, String apellido, String email, String contrasenia,
                         String legajo) {
        //super llama al constructor de Persona para cargar los datos comunes.
        super(dni, nombre, apellido, email, contrasenia);
        this.legajo = legajo;
    }

    //getters:
    public String getLegajo() {
        return legajo;
    }

    //setters:
    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    //metodos:
    //implementa el metodo abstracto de Persona.
    @Override
    public String getTipo() {
        return "Bibliotecario";
    }
    //reutiliza el toString de Persona y le agrega el legajo.
    @Override
    public String toString() {
        return super.toString() + ", Legajo: " + legajo;
    }
}