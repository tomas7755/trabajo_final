package Dao;
import Conexion.Conexion;
import excepciones.ErrorEnBasesDeDatosException;
import modelo.Libro;
import modelo.Reserva;
import modelo.personas.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class ReservaDao {

    //inserta los datos de la reserva
    public void insertar(Reserva r) {
        String sql = "INSERT INTO reserva (numero_reserva, dni_usuario, numero_libro) VALUES (?,?,?)";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getNumeroReserva());
            ps.setString(2, r.getUsuario().getDni());
            ps.setInt(3, r.getLibro().getNumeroIdentificador());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al guardar la reserva." + e.getMessage());
        }
    }

    //elimina la reserva
    public void eliminar(int numeroReserva) {
        String sql = "DELETE FROM reserva WHERE numero_reserva=?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroReserva);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al eliminar la reserva." + e.getMessage());
        }
    }

    //actualiza los datos cargados
    public void actualizar(Reserva r) {
        String sql = "UPDATE reserva SET dni_usuario=? WHERE numero_reserva=?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getUsuario().getDni());
            ps.setInt(2, r.getNumeroReserva());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al modificar la reserva." + e.getMessage());
        }
    }

    //recibe un mapa ya cargado para reconectar cada reserva con sus objetos usuario y libro
    public List<Reserva> listar(Map<String, Usuario> usuarios, Map<Integer, Libro> libros) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reserva";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Reserva(rs.getInt("numero_reserva"), usuarios.get(rs.getString("dni_usuario")), libros.get(rs.getInt("numero_libro"))));
            }
        }
        catch (SQLException e) {
            throw new ErrorEnBasesDeDatosException("Error al leer las reservas." + e.getMessage());
        }
        return lista;
    }
}
