package Dao;
import Conexion.Conexion;
import enums.Disponibilidad;
import excepciones.ErrorEnBasesDeDatosException;
import modelo.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDao {

    //insertar el libro.
    public void insertar(Libro libro){
        String sql = "INSERT INTO libro (numero_identificador, nombre, autor, genero, disponibilidad, activo) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection co = Conexion.getConexion(); PreparedStatement ps = co.prepareStatement(sql)){
            ps.setInt(1, libro.getNumeroIdentificador());
            ps.setString(2, libro.getNombre());
            ps.setString(3, libro.getAutor());
            ps.setString(4, libro.getGenero());
            ps.setString(5, libro.getEstaDispponible().name());
            ps.setBoolean(6, libro.isActivo());
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new ErrorEnBasesDeDatosException("Error al insertar los libros" + e.getMessage());
        }
    }

    //permite modificar los dartos de los libros, modificar la disponibilidad y dar de baja o reactivar el libro
    public void actualizar(Libro libro) {
        String sql = "UPDATE libro SET nombre=?, autor=?, genero=?, disponibilidad=?, activo=? WHERE numero_identificador=?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, libro.getNombre());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getGenero());
            ps.setString(4, libro.getEstaDispponible().name());
            ps.setBoolean(5, libro.isActivo());
            ps.setInt(6, libro.getNumeroIdentificador());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al actualizar el libro" + e.getMessage());
        }
    }

    //crea una lista de libros
    public List<Libro> listar() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libro";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Libro libro = new Libro(rs.getInt("numero_identificador"), rs.getString("nombre"), rs.getString("autor"), rs.getString("genero"));
                libro.setEstaDispponible(Disponibilidad.valueOf(rs.getString("disponibilidad")));
                libro.setActivo(rs.getBoolean("activo"));
                lista.add(libro);
            }
        }
        catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al leer los libros." + e.getMessage());
        }
        return lista;
    }
}

