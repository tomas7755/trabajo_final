package bibliotecario;
import Excepciones.LibroNoDisponible;
import Excepciones.SinLibrosException;
import Excepciones.SinUsuarioException;
import Usuario.Usuario;
import Enum.Disponibilidad;
import java.util.HashMap;
import java.util.Map;
public class Bibliotecario {
    private String nombre;
    private String legajo;
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, Libro> libros = new HashMap<>();
    private Map<Integer, Prestamo> prestamos = new HashMap<>();
    private Map<Integer, Reserva> reservas = new HashMap<>();
    //constructores:
    public Bibliotecario(){}
    public Bibliotecario(String nombre, String legajo) {
        this.nombre = nombre;
        this.legajo = legajo;
    }
    public Bibliotecario(Map<String, Usuario> usuarios, Map<String, Libro> libros, Map<Integer, Prestamo> prestamos, Map<Integer, Reserva> reservas) {
        this.usuarios = usuarios;
        this.libros = libros;
        this.prestamos = prestamos;
        this.reservas = reservas;
    }
    public Bibliotecario(String nombre, String legajo, Map<String, Usuario> usuarios, Map<String, Libro> libros, Map<Integer, Prestamo> prestamos, Map<Integer, Reserva> reservas) {
        this.nombre = nombre;
        this.legajo = legajo;
        this.usuarios = usuarios;
        this.libros = libros;
        this.prestamos = prestamos;
        this.reservas = reservas;
    }
    //getters:
    public String getNombre() {
        return nombre;
    }
    public String getLegajo() {
        return legajo;
    }
    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }
    public Map<String, Libro> getLibros() {
        return libros;
    }
    public Map<Integer, Prestamo> getPrestamos() {
        return prestamos;
    }
    public Map<Integer, Reserva> getReservas() {
        return reservas;
    }
    //setters:
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }
    public void setUsuarios(Map<String, Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    public void setLibros(Map<String, Libro> libros) {
        this.libros = libros;
    }
    public void setPrestamos(Map<Integer, Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
    public void setReservas(Map<Integer, Reserva> reservas) {
        this.reservas = reservas;
    }
    //metodos:
    //metodos para agregar cada uno de los objetos por separado:
    public boolean agregarUsuario(Usuario agregado){
        if(usuarios.containsKey(agregado.getDni())){
            return false;
        }
        usuarios.put(agregado.getDni(), agregado);
        return true;
    }
    public boolean agregarLibro(Libro agregado){
        if (libros.containsKey(agregado.getNombre()))
        {
            return false;
        }
        libros.put(agregado.getNombre(), agregado);

        return true;
    }
    public boolean agregarPrestamo(String dni, String libroPrestamo)throws SinLibrosException, SinUsuarioException, LibroNoDisponible {
        //primero buscamos al usuario y el libro a prestar.
        Usuario usuarioBuscado = usuarios.get(dni);
        Libro libro =  libros.get(libroPrestamo);
        //si las casillas se dejan vacias se tira la excepcion.
        if (libro == null){
            throw new SinLibrosException("La casilla libro esta vacia.");
        }
        if (usuarioBuscado == null){
            throw new SinUsuarioException("La casilla usuario esta vacia.");
        }
        //si el libro ya está prestado, avisa de su desponibilidad.
        if (libro.getEstaDispponible() == Disponibilidad.PRESTADO){
            throw new LibroNoDisponible("El libro ya esta prestado.");
        }
        //si el libro ya está reservado, primero busca si la persona que tiene la reserva
        //es la misma que el nuevo usuario que quiere el prestamo ahora
        if (libro.getEstaDispponible() == Disponibilidad.RESERVADO){

            Reserva reserva = reservas.get(libroPrestamo);

            //si no es la misma persona, tira la excepcion.
            if (reserva == null || !reserva.getUsuario().equals(usuarioBuscado)){
                throw new LibroNoDisponible("El libro esta reservado por otro usuario.");
            }

            //si si es la misma persona, elimina el libro de los reservados
            reservas.remove(libroPrestamo);
        }
        //y se agrega a la desponibilidad de prestado.
        libro.setEstaDispponible(Disponibilidad.PRESTADO);
        return true;
    }
    public boolean agregarReservas(Reserva agregado){
        if (reservas.containsKey(agregado.getNumeroReserva())){
            return false;
        }

        reservas.put(agregado.getNumeroReserva(), agregado);

        return true;
    }

}
