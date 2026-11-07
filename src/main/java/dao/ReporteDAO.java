package dao;

import database.Conexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.entity.CuotaEnMora;
import model.entity.ResumenCartera;

/** Consultas de solo lectura para los reportes. */
public class ReporteDAO {

    Conexion c = new Conexion();

    public List<ResumenCartera> resumenCartera() {
        List<ResumenCartera> filas = new ArrayList<>();
        String sql = "select pr.estado, count(*) as cantidad, sum(pr.monto) as monto, "
                + "coalesce(sum(cu.pendiente), 0) as saldo from prestamos pr "
                + "left join (select prestamo_id, sum(valor - abonado) as pendiente from cuotas group by prestamo_id) cu "
                + "on cu.prestamo_id = pr.id group by pr.estado order by pr.estado";
        try (Connection con = c.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                filas.add(new ResumenCartera(rs.getString("estado"), rs.getInt("cantidad"),
                        rs.getDouble("monto"), rs.getDouble("saldo")));
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de generar el resumen de cartera!\n" + ex.getMessage());
        }
        return filas;
    }

    public List<CuotaEnMora> cuotasEnMora() {
        List<CuotaEnMora> filas = new ArrayList<>();
        String sql = "select cu.prestamo_id, p.nombre, p.documento, p.telefono, cu.numero, cu.fecha_vencimiento, "
                + "cu.valor - cu.abonado as pendiente, datediff(curdate(), cu.fecha_vencimiento) as dias "
                + "from cuotas cu join prestamos pr on pr.id = cu.prestamo_id join personas p on p.id = pr.cliente_id "
                + "where cu.estado = 'VENCIDA' order by dias desc, cu.prestamo_id, cu.numero";
        try (Connection con = c.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                filas.add(new CuotaEnMora(rs.getInt("prestamo_id"), rs.getString("nombre"), rs.getString("documento"),
                        rs.getString("telefono"), rs.getInt("numero"), rs.getDate("fecha_vencimiento").toLocalDate(),
                        rs.getDouble("pendiente"), rs.getInt("dias")));
            }
        } catch (SQLException ex) {
            System.out.println("Error al momento de consultar la mora!\n" + ex.getMessage());
        }
        return filas;
    }
}
