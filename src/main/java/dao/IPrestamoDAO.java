package dao;

import java.util.List;
import model.entity.EstadoPrestamo;
import model.entity.Prestamo;
import model.entity.Solicitud;

/**operaciones de prestamos */
public interface IPrestamoDAO {

    /** Mtdo  aprobar y guardar el prestamo */
    boolean aprobar(Solicitud solicitud, Prestamo prestamo);

    /** con sus cuotas */
    Prestamo buscar(int id);

    /** traer todos los prestamos */
    List<Prestamo> listar();

    /** traer los prestamos de un cliente */
    List<Prestamo> listarPorCliente(int clienteId);

    /**  traer los prestamos por estado */
    List<Prestamo> listarPorEstado(EstadoPrestamo estado);

    /** cuotas  */
    int actualizarVencimientos();

    boolean actualizarEstado(int id, EstadoPrestamo estado);

    /** los prestamos con sus cuotas */
    List<Prestamo> listarConPlan();
}
