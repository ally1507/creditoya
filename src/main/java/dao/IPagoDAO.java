package dao;

import java.time.LocalDate;
import java.util.List;
import model.entity.Cuota;
import model.entity.Pago;
import model.entity.Prestamo;

/** Intrfz con las operaciones de pagos */
public interface IPagoDAO {

    /** Mtdo para guardar un pago */
    boolean registrar(Pago pago, Prestamo prestamo, List<Cuota> cuotasAbonadas);

    /**traer los pagos de un prestamo */
    List<Pago> listarPorPrestamo(int prestamoId);

    /**  traer los pagos de un cliente */
    List<Pago> listarPorCliente(int clienteId);

    /**  traer los pagos entre fechas */
    List<Pago> listarPorFechas(LocalDate desde, LocalDate hasta);
}
