package dao;

import java.util.List;
import model.entity.EstadoPrestamo;
import model.entity.Prestamo;
import model.entity.Solicitud;

/** Contrato de acceso a datos de los prestamos y su plan de cuotas. */
public interface IPrestamoDAO {

    /** Aprueba la solicitud y guarda el prestamo con todas sus cuotas. */
    boolean aprobar(Solicitud solicitud, Prestamo prestamo);

    /** Busca un prestamo con su plan de cuotas cargado. */
    Prestamo buscar(int id);

    List<Prestamo> listar();

    List<Prestamo> listarPorCliente(int clienteId);

    List<Prestamo> listarPorEstado(EstadoPrestamo estado);

    /**
     * Marca como VENCIDAS las cuotas cuya fecha ya paso sin pagarse y como
     * VENCIDOS los prestamos que tengan alguna. Devuelve cuantas cuotas cambiaron.
     */
    int actualizarVencimientos();

    /** Cambia el estado de un prestamo (opcion manual del menu). */
    boolean actualizarEstado(int id, EstadoPrestamo estado);

    /** Todos los prestamos con su plan de cuotas (para guardarlos en el archivo). */
    List<Prestamo> listarConPlan();
}
