package Dao;
import Conexion.Conexion;
import excepciones.ErrorEnBasesDeDatosException;
import modelo.personas.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class PersonaDao {
    //inserta los datos
    public void insertar(Persona persona){
        String sql = "INSERT INTO persona (dni, tipo, nombre, apellido, email, contrasenia, activo, legajo, direccion, telefono)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)){
            cargarParametros(ps, persona);
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new ErrorEnBasesDeDatosException("Error al guardar persona" + e.getMessage());
        }
    }

    //actualiza todos los datos menos el dni que es la clave.
    public void actualizar(Persona persona){
        String sql = "UPDATE persona SET nombre = ?, apellido = ?, email = ?, contrasenia = ?, activo = ? , legajo = ?, direccion = ?, telefono = ? WHERE dni = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellido());
            ps.setString(3, persona.getEmail());
            ps.setString(4, persona.getContrasenia());
            ps.setBoolean(5, persona.isActivo());
            ps.setString(6, persona instanceof Bibliotecario ? ((Bibliotecario) persona).getLegajo() : null);
            ps.setString(7, persona instanceof Usuario ? ((Usuario) persona).getDireccion() : null);
            ps.setString(8, persona instanceof Usuario ? ((Usuario) persona).getTelefono() : null);
            ps.setString(9, persona.getDni());
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new ErrorEnBasesDeDatosException("Error al actualizar la persona" + e.getMessage());
        }
    }

    //actualizar los activos de persona
    public void actualizarActivo(String dni, boolean activo){
        String sql = "UPDATE persona Set activo = ? WHERE dni = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setBoolean(1, activo);
            ps.setString(2, dni);
            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new ErrorEnBasesDeDatosException("Error al actualizar el activo" + e.getMessage());
        }
    }

    //listar bibliotecarios y administradores (lo usa gestorPersonas)
    public List<Persona> listarPersonal(){
        //retorna a todas las personas con un tipo diferente de usuario
        return listar("SELECT * FROM persona WHERE tipo <> 'USUARIO'");
    }

    //listar usuarios (lo usa gestorBibliotecas)
    public List<Usuario> listarUsuarios(){
        List<Usuario> usuarios = new ArrayList<>();
        for (Persona persona : listar("SELECT * FROM persona WHERE  tipo = 'USUARIO'")){
            usuarios.add((Usuario) persona);
        }
        return usuarios;
    }

    //privados

    //listar
    private List<Persona> listar(String sql){
        List<Persona> lista = new ArrayList<>();
        try (Connection co = Conexion.getConexion(); PreparedStatement ps = co.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                lista.add(armarPersona(rs));
            }
        }
        catch (SQLException e){
            throw new ErrorEnBasesDeDatosException("Error al leer a las personas" + e.getMessage());
        }
        return lista;
    }


    private void cargarParametros(PreparedStatement ps, Persona persona) throws SQLException{
        ps.setString(1, persona.getDni());
        ps.setString(2, persona.getTipo().toUpperCase());
        ps.setString(3, persona.getNombre());
        ps.setString(4, persona.getApellido());
        ps.setString(5, persona.getEmail());
        ps.setString(6, persona.getContrasenia());
        ps.setBoolean(7, persona.isActivo());
        ps.setString(8, persona instanceof Bibliotecario ? ((Bibliotecario)persona).getLegajo() : null);
        ps.setString(9, persona instanceof Usuario ? ((Usuario)persona).getDireccion() : null);
        ps.setString(10, persona instanceof Usuario ? ((Usuario)persona).getTelefono() : null);

    }

    //segun la columna de tipo, se crea la clase hija que corresponde (polimorfismo).
    private Persona armarPersona(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        String dni = rs.getString("dni");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String email = rs.getString("email");
        String contrasenia = rs.getString("contrasenia");

        Persona p;
        switch (tipo) {
            case "BIBLIOTECARIO":
                p = new Bibliotecario(dni, nombre, apellido, email, contrasenia, rs.getString("legajo"));
                break;
            case "ADMINISTRADOR":
                p = new Administrador(dni, nombre, apellido, email, contrasenia);
                break;
            default:
                p = new Usuario(dni, nombre, apellido, email, contrasenia,
                        rs.getString("direccion"), rs.getString("telefono"));
        }
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }
}
