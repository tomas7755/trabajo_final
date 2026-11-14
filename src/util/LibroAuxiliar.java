package util;
import modelo.Libro;
import modelo.personas.Usuario;
import enums.Disponibilidad;

/**
 * Clase auxiliar que junta un usuario, un libro y el estado (prestado o reservado)
 * para mostrar todo junto en las consultas del bibliotecario.
 */
public class LibroAuxiliar {
    private Usuario usuario;
    private Libro libro;
    private Disponibilidad estaDisponible;
    //constructor:
    public LibroAuxiliar(){}
    public LibroAuxiliar(Usuario usuario, Libro libro, Disponibilidad estaDisponible){
        this.usuario = usuario;
        this.libro = libro;
        this.estaDisponible = estaDisponible;
    }
    //getters:
    public Usuario getUsuario() {
        return usuario;
    }
    public Libro getLibro() {
        return libro;
    }
    public Disponibilidad getEstaDisponible() {
        return estaDisponible;
    }
    //setters:
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    public void setEstaDisponible(Disponibilidad estaDisponible) {
        this.estaDisponible = estaDisponible;
    }
    //metodos:
    @Override
    public String toString() {
        return "LibroAuxiliar{" + "usuario=" + usuario + ", libro=" + libro + ", estaDisponible=" + estaDisponible + '}';
    }
}
