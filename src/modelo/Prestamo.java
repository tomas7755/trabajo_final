package modelo;
import modelo.personas.Usuario;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa el prestamo de un libro a un usuario.
 * Guarda la fecha en que se presto y la fecha limite para devolverlo (15 dias despues).
 */
public final class Prestamo {
    //numero que identifica al prestamo (es la clave en el Map del gestor).
    private int numeroPrestamo = 0;
    private Usuario usuario;
    private Libro libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    //constructor:
    public Prestamo(){}
    public Prestamo(int numeroPrestamo, Usuario usuario, Libro libro){
        this.numeroPrestamo = numeroPrestamo;
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = LocalDate.now();
        this.fechaDevolucion = fechaPrestamo.plusDays(15);
    }
    public Prestamo(int numeroPrestamo, Usuario usuario, Libro libro, LocalDate fechaPrestamo, LocalDate fechaDevolucion){
        this.numeroPrestamo = numeroPrestamo;
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }
    //getters:
    public int getNumeroPrestamo(){
        return numeroPrestamo;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public Libro getLibro() {
        return libro;
    }
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }
    //setters:
    public void setNumeroPrestamo(int numeroPrestamo) {
        this.numeroPrestamo = numeroPrestamo;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    //metodos:
    //extiende la fecha de devolucion la cantidad de dias indicada (renovacion del prestamo).
    public void renovar(int dias) {
        this.fechaDevolucion = fechaDevolucion.plusDays(dias);
    }
    //dos prestamos son iguales si tienen el mismo numero de prestamo.
    //(antes comparaba por usuario, y dos prestamos del mismo usuario quedaban "iguales").
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Prestamo prestamo = (Prestamo) o;
        return numeroPrestamo == prestamo.numeroPrestamo;
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(numeroPrestamo);
    }
    @Override
    public String toString() {
        return "Prestamo{" + "numeroPrestamo=" + numeroPrestamo + ", usuario=" + usuario + ", libro=" + libro + ", fechaPrestamo=" + fechaPrestamo + ", fechaDevolucion=" + fechaDevolucion + '}';
    }
}
