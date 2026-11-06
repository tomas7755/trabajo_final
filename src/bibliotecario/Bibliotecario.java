package bibliotecario;
import Usuario.Usuario;
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

}
