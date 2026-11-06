package bibliotecario;
import Usuario.Usuario;

import java.util.Objects;

public final class Reserva {
    private int numeroReserva = 0;
    private Usuario usuario;
    private Libro libro;
    //constructor:
    public Reserva(){}
    public Reserva(int numeroReserva){}
    public Reserva(int numeroReserva, Usuario usuario, Libro libro){
        this.numeroReserva = numeroReserva;
        this.usuario = usuario;
        this.libro = libro;
    }
    //getters:
    public int getNumeroReserva(){
        return numeroReserva;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public Libro getLibro() {
        return libro;
    }
    //setters:
    public void setNumeroReserva(int numeroReserva) {
        this.numeroReserva = numeroReserva;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    //metodos:
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(usuario, reserva.usuario);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(usuario);
    }
    @Override
    public String toString() {
        return "Reserva{" + "numeroReserva=" + numeroReserva + ", usuario=" + usuario + ", libro=" + libro + '}';
    }
}
