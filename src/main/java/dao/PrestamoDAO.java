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
import model.entity.Cuota;
import model.entity.EstadoCuota;
import model.entity.EstadoPrestamo;
import model.entity.Prestamo;
import model.entity.Solicitud;

/** Cls para guardar y consultar prestamos y cuotas en MySQL */
public class PrestamoDAO implements IPrestamoDAO {

    Conexion c = new Conexion();

    private static final String SELECT_PRESTAMO
            = "select pr.*, p.nombre as nombre_cliente from prestamos pr join personas p on p.id = pr.cliente_id ";

    /** Mtdo para aprobar la solicitud y guardar prestamo y cuotas */
    @Override
    public boolean aprobar(Solicitud s, Prestamo pr) {
        String sqlSolicitud = "update solicitudes set estado='APROBADA', observaciones=? where id=? and estado='PENDIENTE'";
        String sqlPrestamo = "insert into prestamos(cliente_id, empleado_id, solicitud_id, monto, tasa_ea, tasa_mensual, "
                + "cuotas, total_pagar, fecha_inicio, fecha_fin, estado) values (?,?,?,?,?,?,?,?,?,?,?)";
        String sqlCuota = "insert into cuotas(prestamo_id, numero, fecha_vencimiento, capital, interes, valor, abonado, saldo, estado) "
                + "values (?,?,?,?,?,?,?,?,?)";
        try (Connection con = c.conectar()) {
            try (PreparedStatement ps = con.prepareStatement(sqlSolicitud)) {
                ps.setString(1, s.getObservaciones());
                ps.setInt(2, s.getId());
                if (ps.executeUpdate() == 0) {
                    System.out.println("La solicitud ya no esta pendiente");
                    return false;
                }
            }
            try {
                try (PreparedStatement ps = con.prepareStatement(sqlPrestamo, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, pr.getClienteId());
                    ps.setInt(2, pr.getEmpleadoId());
                    ps.setInt(3, pr.getSolicitudId());
                    ps.setDouble(4, pr.getMonto());
                    ps.setDouble(5, pr.getTasaEa());
                    ps.setDouble(6, pr.getTasaMensual());
                    ps.setInt(7, pr.getCuotas());
                    ps.setDouble(8, pr.getTotalPagar());
                    ps.setDate(9, Date.valueOf(pr.getFechaInicio()));
                    ps.setDate(10, Date.valueOf(pr.getFechaFin()));
                    ps.setString(11, pr.getEstado().name());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            pr.setId(rs.getInt(1));
                        }
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(sqlCuota)) {
                    for (Cuota cu : pr.getPlan()) {
                        ps.setInt(1, pr.getId());
                        ps.setInt(2, cu.getNumero());
                        ps.setDate(3, Date.valueOf(cu.getFechaVencimiento()));
                        ps.setDouble(4, cu.getCapital());
                        ps.setDouble(5, cu.getInteres());
                        ps.setDouble(6, cu.getValor());
                        ps.setDouble(7, cu.getAbonado());
                        ps.setDouble(8, cu.getSaldo());
                        ps.setString(9, cu.getEstado().name());
                        ps.executeUpdate();
                    }
                }
                return true;
            } catch (SQLException ex) {
                deshacerAprobacion(con, pr.getId(), s.getId());
                throw ex;
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de aprobar la solicitud!\n" + ex.getMessage());
        }
        return false;
    }

    /** Mtdo para deshacer una aprobacion que fallo */
    private void deshacerAprobacion(Connection con, int prestamoId, int solicitudId) {
        try (PreparedStatement ps = con.prepareStatement("delete from prestamos where id=?");
                PreparedStatement ps2 = con.prepareStatement("update solicitudes set estado='PENDIENTE' where id=?")) {
            ps.setInt(1, prestamoId);
            ps.executeUpdate();
            ps2.setInt(1, solicitudId);
            ps2.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al deshacer la aprobacion!\n" + ex.getMessage());
        }
    }

    /** Mtdo para buscar un prestamo con sus cuotas */
    @Override
    public Prestamo buscar(int id) {
        String sql = SELECT_PRESTAMO + "where pr.id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    Prestamo p = armarPrestamo(rs);
                    p.setPlan(listarCuotas(con, id));
                    return p;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de buscar el prestamo!\n" + ex.getMessage());
        }
        return null;
    }

    /** Mtdo para traer todos los prestamos */
    @Override
    public List<Prestamo> listar() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = SELECT_PRESTAMO + "order by pr.id";
        try (Connection con = c.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                prestamos.add(armarPrestamo(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar los prestamos!\n" + ex.getMessage());
        }
        return prestamos;
    }

    /** Mtdo para traer los prestamos de un cliente */
    @Override
    public List<Prestamo> listarPorCliente(int clienteId) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = SELECT_PRESTAMO + "where pr.cliente_id=? order by pr.id";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    prestamos.add(armarPrestamo(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar los prestamos!\n" + ex.getMessage());
        }
        return prestamos;
    }

    /** Mtdo para traer los prestamos por estado */
    @Override
    public List<Prestamo> listarPorEstado(EstadoPrestamo estado) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = SELECT_PRESTAMO + "where pr.estado=? order by pr.id";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, estado.name());
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    prestamos.add(armarPrestamo(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar los prestamos!\n" + ex.getMessage());
        }
        return prestamos;
    }

    /** Mtdo para marcar las cuotas vencidas */
    @Override
    public int actualizarVencimientos() {
        String sqlCuotas = "update cuotas set estado='VENCIDA' where estado in ('PENDIENTE','PARCIAL') and fecha_vencimiento < curdate()";
        String sqlPrestamos = "update prestamos pr set estado='VENCIDO' where estado='ACTIVO' and exists "
                + "(select 1 from cuotas cu where cu.prestamo_id = pr.id and cu.estado='VENCIDA')";
        try (Connection con = c.conectar(); Statement st = con.createStatement()) {
            int cuotas = st.executeUpdate(sqlCuotas);
            st.executeUpdate(sqlPrestamos);
            return cuotas;
        } catch (SQLException ex) {
            System.out.println("Error al momento de actualizar los vencimientos!\n" + ex.getMessage());
        }
        return 0;
    }

    /** Mtdo para cambiar el estado de un prestamo */
    @Override
    public boolean actualizarEstado(int id, EstadoPrestamo estado) {
        String sql = "update prestamos set estado=? where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, estado.name());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al momento de cambiar el estado del prestamo!\n" + ex.getMessage());
        }
        return false;
    }

    /** Mtdo para traer los prestamos con sus cuotas */
    @Override
    public List<Prestamo> listarConPlan() {
        List<Prestamo> prestamos = new ArrayList<>();
        for (Prestamo p : listar()) {
            prestamos.add(buscar(p.getId()));
        }
        return prestamos;
    }

    /** Mtdo para traer las cuotas de un prestamo */
    private List<Cuota> listarCuotas(Connection con, int prestamoId) throws SQLException {
        List<Cuota> cuotas = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("select * from cuotas where prestamo_id=? order by numero")) {
            ps.setInt(1, prestamoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cuotas.add(new Cuota(rs.getInt("id"), rs.getInt("numero"),
                            rs.getDate("fecha_vencimiento").toLocalDate(), rs.getDouble("capital"),
                            rs.getDouble("interes"), rs.getDouble("valor"), rs.getDouble("abonado"),
                            rs.getDouble("saldo"), EstadoCuota.valueOf(rs.getString("estado"))));
                }
            }
        }
        return cuotas;
    }

    /** Mtdo para pasar una fila a Prestamo */
    private Prestamo armarPrestamo(ResultSet rs) throws SQLException {
        return new Prestamo(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("empleado_id"),
                rs.getInt("solicitud_id"), rs.getDouble("monto"), rs.getDouble("tasa_ea"),
                rs.getDouble("tasa_mensual"), rs.getInt("cuotas"), rs.getDouble("total_pagar"),
                rs.getDate("fecha_inicio").toLocalDate(), rs.getDate("fecha_fin").toLocalDate(),
                EstadoPrestamo.valueOf(rs.getString("estado")), rs.getString("nombre_cliente"));
    }
}
