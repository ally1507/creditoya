package dao;

import java.util.List;
import model.entity.EstadoSolicitud;
import model.entity.Solicitud;

/**
 * Contrato de acceso a datos de las solicitudes. La aprobacion esta en
 * IPrestamoDAO.aprobar porque ademas crea el prestamo.
 */
public interface ISolicitudDAO {

    boolean agregar(Solicitud solicitud);

    /** Guarda el rechazo (solo si la solicitud sigue PENDIENTE en la base de datos). */
    boolean rechazar(Solicitud solicitud);

    Solicitud buscar(int id);

    List<Solicitud> listar();

    List<Solicitud> listarPorEstado(EstadoSolicitud estado);

    List<Solicitud> listarPorCliente(int clienteId);
}
