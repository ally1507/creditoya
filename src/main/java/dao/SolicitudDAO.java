package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.entity.EstadoSolicitud;
import model.entity.Solicitud;

public class SolicitudDAO implements ISolicitudDAO {

    Conexion c = new Conexion();

    private static final String SELECT_SOLICITUD
            = "select s.*, p.nombre as nombre_cliente from solicitudes s join personas p on p.id = s.cliente_id ";

    @Override
    public boolean agregar(Solicitud s) {
        String sql = "insert into solicitudes(cliente_id, empleado_id, monto, cuotas, fecha_solicitud, estado, observaciones) values (?,?,?,?,?,?,?)";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            ps.setInt(1, s.getClienteId());
            ps.setInt(2, s.getEmpleadoId());
            ps.setDouble(3, s.getMonto());
            ps.setInt(4, s.getCuotas());
            ps.setDate(5, Date.valueOf(s.getFechaSolicitud()));
            ps.setString(6, s.getEstado().name());
            ps.setString(7, s.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    s.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de ingresar la solicitud!\n" + ex.getMessage());
        }
        return false;
    }

    /** Solo rechaza si la solicitud sigue PENDIENTE en la base de datos. */
    @Override
    public boolean rechazar(Solicitud s) {
        String sql = "update solicitudes set estado='RECHAZADA', observaciones=? where id=? and estado='PENDIENTE'";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, s.getObservaciones());
            ps.setInt(2, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al momento de rechazar la solicitud!\n" + ex.getMessage());
        }
        return false;
    }

    @Override
    public Solicitud buscar(int id) {
        String sql = SELECT_SOLICITUD + "where s.id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    return armarSolicitud(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de buscar la solicitud!\n" + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Solicitud> listar() {
        List<Solicitud> solicitudes = new ArrayList<>();
        String sql = SELECT_SOLICITUD + "order by s.id";
        try (Connection con = c.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                solicitudes.add(armarSolicitud(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar las solicitudes!\n" + ex.getMessage());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> listarPorEstado(EstadoSolicitud estado) {
        List<Solicitud> solicitudes = new ArrayList<>();
        String sql = SELECT_SOLICITUD + "where s.estado=? order by s.id";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, estado.name());
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    solicitudes.add(armarSolicitud(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar las solicitudes!\n" + ex.getMessage());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> listarPorCliente(int clienteId) {
        List<Solicitud> solicitudes = new ArrayList<>();
        String sql = SELECT_SOLICITUD + "where s.cliente_id=? order by s.id";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    solicitudes.add(armarSolicitud(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar las solicitudes!\n" + ex.getMessage());
        }
        return solicitudes;
    }

    private Solicitud armarSolicitud(ResultSet rs) throws SQLException {
        return new Solicitud(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("empleado_id"),
                rs.getDouble("monto"), rs.getInt("cuotas"), rs.getDate("fecha_solicitud").toLocalDate(),
                EstadoSolicitud.valueOf(rs.getString("estado")), rs.getString("observaciones"),
                rs.getString("nombre_cliente"));
    }
}
