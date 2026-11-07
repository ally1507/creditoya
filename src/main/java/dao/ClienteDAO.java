package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.entity.Cliente;

/**
 * CRUD de clientes. Un cliente se guarda en dos tablas: personas (datos
 * comunes) y clientes (direccion).
 */
public class ClienteDAO implements IClienteDAO {

    Conexion c = new Conexion();

    private static final String SELECT_CLIENTE
            = "select p.id, p.nombre, p.documento, p.correo, p.telefono, p.clave_hash, "
            + "p.activo, c.direccion from personas p join clientes c on c.persona_id = p.id ";

    @Override
    public boolean agregar(Cliente cl) {
        String sqlPersona = "insert into personas(nombre, documento, correo, telefono, rol, clave_hash) values (?,?,?,?,?,?)";
        String sqlCliente = "insert into clientes(persona_id, direccion) values (?,?)";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getDocumento());
            ps.setString(3, cl.getCorreo());
            ps.setString(4, cl.getTelefono());
            ps.setString(5, cl.getRol().name());
            ps.setString(6, cl.getClave());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cl.setId(rs.getInt(1));
                }
            }
            try (PreparedStatement ps2 = con.prepareStatement(sqlCliente)) {
                ps2.setInt(1, cl.getId());
                ps2.setString(2, cl.getDireccion());
                ps2.executeUpdate();
                return true;
            } catch (SQLException ex) {
                // Si falla la segunda tabla se borra la persona para no dejarla a medias.
                borrarPersona(con, cl.getId());
                throw ex;
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de ingresar el cliente!\n" + ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizar(Cliente cl) {
        String sqlPersona = "update personas set nombre=?, documento=?, correo=?, telefono=?, clave_hash=?, activo=? where id=?";
        String sqlCliente = "update clientes set direccion=? where persona_id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sqlPersona);
                PreparedStatement ps2 = con.prepareStatement(sqlCliente);) {
            ps.setString(1, cl.getNombre());
            ps.setString(2, cl.getDocumento());
            ps.setString(3, cl.getCorreo());
            ps.setString(4, cl.getTelefono());
            ps.setString(5, cl.getClave());
            ps.setBoolean(6, cl.isActivo());
            ps.setInt(7, cl.getId());
            ps.executeUpdate();
            ps2.setString(1, cl.getDireccion());
            ps2.setInt(2, cl.getId());
            ps2.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al momento de actualizar el cliente!\n" + ex.getMessage());
        }
        return false;
    }

    /** Si el cliente tiene solicitudes o prestamos MySQL no lo deja borrar. */
    @Override
    public boolean eliminar(int id) {
        try (Connection con = c.conectar()) {
            return borrarPersona(con, id);
        } catch (SQLException ex) {
            System.out.println("Error al momento de eliminar el cliente!\n" + ex.getMessage());
        }
        return false;
    }

    @Override
    public Cliente buscar(int id) {
        String sql = SELECT_CLIENTE + "where p.id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return armarCliente(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de buscar el cliente!\n" + ex.getMessage());
        }
        return null;
    }

    @Override
    public Cliente buscarPorDocumento(String documento) {
        String sql = SELECT_CLIENTE + "where p.documento=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return armarCliente(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de buscar el cliente!\n" + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = SELECT_CLIENTE + "order by p.id";
        try (Connection con = c.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(armarCliente(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar los clientes!\n" + ex.getMessage());
        }
        return clientes;
    }

    private Cliente armarCliente(ResultSet rs) throws SQLException {
        return new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("documento"),
                rs.getString("correo"), rs.getString("telefono"), rs.getString("clave_hash"),
                rs.getBoolean("activo"), rs.getString("direccion"));
    }

    private boolean borrarPersona(Connection con, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("delete from personas where id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
