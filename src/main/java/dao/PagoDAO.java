package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.entity.Cuota;
import model.entity.MetodoPago;
import model.entity.Pago;
import model.entity.Prestamo;

public class PagoDAO implements IPagoDAO {

    Conexion c = new Conexion();

    /** Guarda el pago, los abonos de las cuotas y el nuevo estado del prestamo. */
    @Override
    public boolean registrar(Pago pago, Prestamo prestamo, List<Cuota> cuotasAbonadas) {
        String sqlPago = "insert into pagos(prestamo_id, empleado_id, fecha_pago, monto, metodo, observaciones) values (?,?,?,?,?,?)";
        String sqlCuota = "update cuotas set abonado=?, estado=? where id=?";
        String sqlPrestamo = "update prestamos set estado=? where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sqlPago, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement ps2 = con.prepareStatement(sqlCuota);
                PreparedStatement ps3 = con.prepareStatement(sqlPrestamo);) {
            ps.setInt(1, pago.getPrestamoId());
            ps.setInt(2, pago.getEmpleadoId());
            ps.setDate(3, Date.valueOf(pago.getFechaPago()));
            ps.setDouble(4, pago.getMonto());
            ps.setString(5, pago.getMetodo().name());
            ps.setString(6, pago.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pago.setId(rs.getInt(1));
                }
            }
            for (Cuota cu : cuotasAbonadas) {
                ps2.setDouble(1, cu.getAbonado());
                ps2.setString(2, cu.getEstado().name());
                ps2.setInt(3, cu.getId());
                ps2.executeUpdate();
            }
            ps3.setString(1, prestamo.getEstado().name());
            ps3.setInt(2, prestamo.getId());
            ps3.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error al momento de registrar el pago!\n" + ex.getMessage());
        }
        return false;
    }

    @Override
    public List<Pago> listarPorPrestamo(int prestamoId) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "select * from pagos where prestamo_id=? order by fecha_pago, id";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, prestamoId);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    pagos.add(armarPago(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar los pagos!\n" + ex.getMessage());
        }
        return pagos;
    }

    @Override
    public List<Pago> listarPorCliente(int clienteId) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "select pa.* from pagos pa join prestamos pr on pr.id = pa.prestamo_id where pr.cliente_id=? order by pa.fecha_pago, pa.id";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    pagos.add(armarPago(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar los pagos!\n" + ex.getMessage());
        }
        return pagos;
    }

    @Override
    public List<Pago> listarPorFechas(LocalDate desde, LocalDate hasta) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "select * from pagos where fecha_pago between ? and ? order by fecha_pago, id";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setDate(1, Date.valueOf(desde));
            ps.setDate(2, Date.valueOf(hasta));
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    pagos.add(armarPago(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de listar los pagos!\n" + ex.getMessage());
        }
        return pagos;
    }

    private Pago armarPago(ResultSet rs) throws SQLException {
        return new Pago(rs.getInt("id"), rs.getInt("prestamo_id"), rs.getInt("empleado_id"),
                rs.getDate("fecha_pago").toLocalDate(), rs.getDouble("monto"),
                MetodoPago.valueOf(rs.getString("metodo")), rs.getString("observaciones"));
    }
}
