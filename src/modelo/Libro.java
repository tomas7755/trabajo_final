package modelo;
import enums.Disponibilidad;

import java.util.Objects;

public final class Libro{
    private int numeroIdentificador = 0;
    private String nombre;
    private String autor;
    private Disponibilidad estaDispponible;
    //sirve para la baja logica: en vez de borrar el libro, se marca como inactivo.
    //se inicializa aca para que todos los constructores lo creen activo.
    private boolean activo = true;
    //constructor
    public Libro() {}
    public Libro(int numeroIdentificador, String nombre, String autor){
        this.numeroIdentificador = numeroIdentificador;
        this.nombre = nombre;
        this.autor = autor;
        this.estaDispponible = Disponibilidad.DISPONIBLE;
    }
    public Libro(int numeroIdentificador, String nombre, String autor, Disponibilidad estaDispponible){
        this.numeroIdentificador = numeroIdentificador;
        this.nombre = nombre;
        this.autor = autor;
        this.estaDispponible = estaDispponible;
    }
    //getters:
    public int getNumeroIdentificador() {
        return numeroIdentificador;
    }
    public String getNombre(){
        return nombre;
    }
    public String getAutor() {
        return autor;
    }
    public Disponibilidad getEstaDispponible(){
        return estaDispponible;
    }
    public boolean isActivo() {
        return activo;
    }
    //setters:
    public void setNumeroIdentificador(int numeroIdentificador) {
        this.numeroIdentificador = numeroIdentificador;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public void setEstaDispponible(Disponibilidad estaDispponible) {
        this.estaDispponible = estaDispponible;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    //metodos:
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return numeroIdentificador == libro.numeroIdentificador;
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(numeroIdentificador);
    }
    @Override
    public String toString() {
        return "Libro{" + "numeroIdentificador=" + numeroIdentificador + ", nombre='" + nombre + '\'' + ", autor='" + autor + '\'' + ", estaDispponible=" + estaDispponible + ", activo=" + activo + '}';
    }
}