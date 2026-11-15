package Dao;
import modelo.personas.*;
import modelo.Libro;
import Conexion.Conexion;
import excepciones.ErrorEnBasesDeDatosException;
import modelo.Prestamo;
import java.sql.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class PrestamoDao {

    //inserta los datos del prestamo
    public void insertar(Prestamo prestamo) {
        String sql = "INSERT INTO prestamo (numero_prestamo, dni_usuario, numero_libro, fecha_prestamo, fecha_devolucion) VALUES (?,?,?,?,?)";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, prestamo.getNumeroPrestamo());
            ps.setString(2, prestamo.getUsuario().getDni());
            ps.setInt(3, prestamo.getLibro().getNumeroIdentificador());
            ps.setDate(4, Date.valueOf(prestamo.getFechaPrestamo()));
            ps.setDate(5, Date.valueOf(prestamo.getFechaDevolucion()));
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al guardar los datos del prestamo" + e.getMessage());
        }
    }

    //actualiza la fecha de devolucion
    public void actualizarFechaDevolucion(Prestamo prestamo) {
        String sql = "UPDATE prestamo SET fecha_devolucion=? WHERE numero_prestamo=?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(prestamo.getFechaDevolucion()));
            ps.setInt(2, prestamo.getNumeroPrestamo());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al renovar el préstamo." + e.getMessage());
        }
    }

    //eliminar un prestamo con su numero de prestamo
    public void eliminar(int numeroPrestamo) {
        String sql = "DELETE FROM prestamo WHERE numero_prestamo=?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroPrestamo);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al eliminar el préstamo." + e.getMessage());
        }
    }

    //actualiza los datos cargados
    public void actualizar(Prestamo prestamo) {
        String sql = "UPDATE prestamo SET dni_usuario=?, fecha_prestamo=?, fecha_devolucion=? WHERE numero_prestamo=?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prestamo.getUsuario().getDni());
            ps.setDate(2, Date.valueOf(prestamo.getFechaPrestamo()));
            ps.setDate(3, Date.valueOf(prestamo.getFechaDevolucion()));
            ps.setInt(4, prestamo.getNumeroPrestamo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al modificar el préstamo." + e.getMessage());
        }
    }

    //recibe un mapa ya cargado para reconectar cada prestamo con sus objetos usuario y libro
    public List<Prestamo> listar(Map<String, Usuario> usuarios, Map<Integer, Libro> libros) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamo";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Prestamo(rs.getInt("numero_prestamo"), usuarios.get(rs.getString("dni_usuario")), libros.get(rs.getInt("numero_libro")), rs.getDate("fecha_prestamo").toLocalDate(), rs.getDate("fecha_devolucion").toLocalDate()));
            }
        }
        catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al leer los préstamos." + e.getMessage());
        }
        return lista;
    }
}

