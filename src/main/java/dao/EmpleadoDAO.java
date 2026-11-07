package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.entity.Empleado;
import model.entity.Rol;

/**
 * CRUD de empleados. Un empleado se guarda en dos tablas: personas (datos
 * comunes) y empleados (salario).
 */
public class EmpleadoDAO implements IEmpleadoDAO {

    Conexion c = new Conexion();

    private static final String SELECT_EMPLEADO
            = "select p.id, p.nombre, p.documento, p.correo, p.telefono, p.rol, p.clave_hash, "
            + "p.activo, e.salario from personas p join empleados e on e.persona_id = p.id ";

    @Override
    public boolean agregar(Empleado e) {
        String sqlPersona = "insert into personas(nombre, documento, correo, telefono, rol, clave_hash) values (?,?,?,?,?,?)";
        String sqlEmpleado = "insert into empleados(persona_id, salario) values (?,?)";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDocumento());
            ps.setString(3, e.getCorreo());
            ps.setString(4, e.getTelefono());
            ps.setString(5, e.getRol().name());
            ps.setString(6, e.getClave());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setId(rs.getInt(1));
                }
            }
            try (PreparedStatement ps2 = con.prepareStatement(sqlEmpleado)) {
                ps2.setInt(1, e.getId());
                ps2.setDouble(2, e.getSalario());
                ps2.executeUpdate();
                return true;
            } catch (SQLException ex) {
                // Si falla la segunda tabla se borra la persona para no dejarla a medias.
                borrarPersona(con, e.getId());
                throw ex;
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de ingresar el empleado!\n" + ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizar(Empleado e) {
        String sqlPersona = "update personas set nombre=?, documento=?, correo=?, telefono=?, rol=?, clave_hash=?, activo=? where id=?";
        String sqlEmpleado = "update empleados set salario=? where persona_id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sqlPersona);
                PreparedStatement ps2 = con.prepareStatement(sqlEmpleado);) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDocumento());
            ps.setString(3, e.getCorreo());
            ps.setString(4, e.getTelefono());
            ps.setString(5, e.getRol().name());
            ps.setString(6, e.getClave());
            ps.setBoolean(7, e.isActivo());
            ps.setInt(8, e.getId());
            ps.executeUpdate();
            ps2.setDouble(1, e.getSalario());
            ps2.setInt(2, e.getId());
            ps2.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al momento de actualizar el empleado!\n" + ex.getMessage());
        }
        return false;
    }

    /**
     * Borra la persona (la fila de empleados se borra sola por ON DELETE CASCADE).
     * Si el empleado tiene solicitudes, prestamos o pagos MySQL no lo deja borrar.
     */
    @Override
    public boolean eliminar(int id) {
        try (Connection con = c.conectar()) {
            return borrarPersona(con, id);
        } catch (SQLException ex) {
            System.out.println("Error al momento de eliminar el empleado!\n" + ex.getMessage());
        }
        return false;
    }

    @Override
    public Empleado buscar(int id) {
        String sql = SELECT_EMPLEADO + "where p.id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return armarEmpleado(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de buscar el empleado!\n" + ex.getMessage());
        }
        return null;
    }

    @Override
    public Empleado buscarPorDocumento(String documento) {
        String sql = SELECT_EMPLEADO + "where p.documento=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return armarEmpleado(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de buscar el empleado!\n" + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Empleado> listar() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = SELECT_EMPLEADO + "order by p.id";
        try (Connection con = c.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                empleados.add(armarEmpleado(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar los empleados!\n" + ex.getMessage());
        }
        return empleados;
    }

    private Empleado armarEmpleado(ResultSet rs) throws SQLException {
        return new Empleado(rs.getInt("id"), rs.getString("nombre"), rs.getString("documento"),
                rs.getString("correo"), rs.getString("telefono"), Rol.valueOf(rs.getString("rol")),
                rs.getString("clave_hash"), rs.getBoolean("activo"), rs.getDouble("salario"));
    }

    private boolean borrarPersona(Connection con, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("delete from personas where id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
