package modelo.personas;

import java.util.Objects;

/**
 * Clase abstracta que reune los datos comunes a todas las personas del sistema
 * (usuarios, administradores, bibliotecarios).
 * Es abstracta porque nunca se crea una "Persona" sola: siempre es de algun tipo concreto.
 */

public abstract class Persona {
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasenia;
    //sirve para la baja logica: en vez de borrar a la persona, se marca como inactiva.
    private boolean activo;

    //constructores:
    public Persona() {
        this.activo = true;
    }
    public Persona(String dni, String nombre, String apellido, String email, String contrasenia) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        //toda persona nueva arranca activa.
        this.activo = true;
    }

    //getters:
    public String getDni() {
        return dni;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getEmail() {
        return email;
    }
    public String getContrasenia() {
        return contrasenia;
    }
    public boolean isActivo() {
        return activo;
    }

    //setters:
    public void setDni(String dni) {
        this.dni = dni;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    //metodos:
    //metodo abstracto: cada clase hija dice que tipo de persona es (polimorfismo).
    public abstract String getTipo();

    //dos personas son iguales si tienen el mismo dni.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(dni, persona.dni);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(dni);
    }
    @Override
    public String toString() {
        return getTipo() + " - DNI: " + dni +
                ", Nombre: " + nombre + " " + apellido +
                ", Email: " + email +
                ", Estado: " + (activo ? "Activo" : "Inactivo");
    }
}