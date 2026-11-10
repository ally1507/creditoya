package dao;

import java.util.List;
import model.entity.EstadoSolicitud;
import model.entity.Solicitud;

/**operaciones de solicitudes */
public interface ISolicitudDAO {

    /**guardar una solicitud */
    boolean agregar(Solicitud solicitud);

    boolean rechazar(Solicitud solicitud);

    Solicitud buscar(int id);

    /**  todas */
    List<Solicitud> listar();

    List<Solicitud> listarPorEstado(EstadoSolicitud estado);

    /**solicitudes de un cliente */
    List<Solicitud> listarPorCliente(int clienteId);
}
