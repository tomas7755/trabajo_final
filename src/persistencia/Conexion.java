package persistencia;
import excepciones.ErrorEnBasesDeDatosException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Arma la conexion JDBC con la base de datos MySQL "biblioteca".
 * El script para crear la base y cargar los datos esta en bd/biblioteca.sql.
 */
public class Conexion {
    //la parte que dice "&serverTimezone=America/Argentina/Buenos_Aires" es para que use la franja horaria que va a utilizar la base de datos.
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USUARIO = "root";
    private static final String CLAVE = "1234";

    //cada dao pide una conexion nueva y la cierra con el try-with-resources
    public static Connection getConexion(){
        try{
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        }
        catch (SQLException e){
            throw new ErrorEnBasesDeDatosException("No se puede conectar con la base de datos. " + e.getMessage());
        }
    }
}
