import vista.Gestion;
import javafx.application.Application;

/**
 * Punto de entrada del programa: abre la ventana principal (Gestion).
 * Antes de ejecutarlo hay que crear la base de datos con el script bd/biblioteca.sql
 * (los datos de conexion estan en persistencia/Conexion.java).
 */
public class Main {
    public static void main(String[] args) {
        Application.launch(Gestion.class, args);
    }
}
