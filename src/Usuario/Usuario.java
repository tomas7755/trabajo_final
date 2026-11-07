package Usuario;

import java.util.Objects;

public class Usuario {
    private String dni;
    private String nombre;
    //constructor:

    //getters:
    public String getDni() {
        return dni;
    }
    public String getNombre() {
        return nombre;
    }
    //setters:

    //metodos:
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(dni, usuario.dni);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(dni);
    }
}
