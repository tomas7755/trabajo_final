package modelo;
import modelo.personas.Usuario;
import java.time.LocalDate;
import java.util.Objects;

public final class Prestamo {
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
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Prestamo prestamo = (Prestamo) o;
        return Objects.equals(usuario, prestamo.usuario);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(usuario);
    }
    @Override
    public String toString() {
        return "Prestamo{" + "numeroPrestamo=" + numeroPrestamo + ", usuario=" + usuario + ", libro=" + libro + ", fechaPrestamo=" + fechaPrestamo + ", fechaDevolucion=" + fechaDevolucion + '}';
    }
}
