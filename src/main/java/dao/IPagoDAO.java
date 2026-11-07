package dao;

import java.time.LocalDate;
import java.util.List;
import model.entity.Cuota;
import model.entity.Pago;
import model.entity.Prestamo;

/** Contrato de acceso a datos de los pagos. */
public interface IPagoDAO {

    /** Guarda el pago, los abonos de las cuotas y el nuevo estado del prestamo. */
    boolean registrar(Pago pago, Prestamo prestamo, List<Cuota> cuotasAbonadas);

    List<Pago> listarPorPrestamo(int prestamoId);

    List<Pago> listarPorCliente(int clienteId);

    List<Pago> listarPorFechas(LocalDate desde, LocalDate hasta);
}
