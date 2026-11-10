package controller;

import dao.IPagoDAO;
import dao.IPrestamoDAO;
import dao.ISolicitudDAO;
import dao.PagoDAO;
import dao.PrestamoDAO;
import dao.SolicitudDAO;
import java.util.List;
import model.entity.Pago;
import model.entity.Prestamo;
import model.entity.Solicitud;
import model.logica.UsuarioSesion;
import view.Mensajes;
import view.pago.imprimir.ImprimirPago;
import view.prestamo.imprimir.ImprimirPrestamo;
import view.prestamo.ingreso.IngresoPrestamo;
import view.solicitud.imprimir.ImprimirSolicitud;

/** Cls de consultas del cliente en sesion */
public class PortalClienteController {

    private final IPrestamoDAO prestamoDao = new PrestamoDAO();
    private final IPagoDAO pagoDao = new PagoDAO();
    private final ISolicitudDAO solicitudDao = new SolicitudDAO();
    private final ImprimirPrestamo imprimirPrestamo = new ImprimirPrestamo();
    private final ImprimirPago imprimirPago = new ImprimirPago();
    private final ImprimirSolicitud imprimirSolicitud = new ImprimirSolicitud();
    private final IngresoPrestamo ingreso = new IngresoPrestamo();
    private final Mensajes mensajes = new Mensajes();

    /** Mtdo que devuelve el id del cliente en sesion */
    private int idCliente() {
        return UsuarioSesion.getInstance().getUsuario().getId();
    }

    /** Mtdo para ver mis prestamos */
    public void misPrestamos() {
        mensajes.informativo("===============Mis prestamos===============");
        List<Prestamo> prestamos = prestamoDao.listarPorCliente(idCliente());
        if (prestamos.isEmpty()) {
            mensajes.informativo("No tiene prestamos");
        } else {
            imprimirPrestamo.list(prestamos);
        }
    }

    /** Mtdo para ver el plan de cuotas de mi prestamo */
    public void planDeCuotas() {
        mensajes.informativo("===============Plan de cuotas===============");
        Prestamo p = prestamoDao.buscar(ingreso.pedirId());
        // Si es de otro cliente, como si no existiera
        if (p == null || p.getClienteId() != idCliente()) {
            mensajes.error("No tiene un prestamo con ese id");
        } else {
            imprimirPrestamo.plan(p);
        }
    }

    /** Mtdo para ver mis pagos */
    public void misPagos() {
        mensajes.informativo("===============Mis pagos===============");
        List<Pago> pagos = pagoDao.listarPorCliente(idCliente());
        if (pagos.isEmpty()) {
            mensajes.informativo("No tiene pagos registrados");
        } else {
            imprimirPago.list(pagos);
        }
    }

    /** Mtdo para ver mis solicitudes */
    public void misSolicitudes() {
        mensajes.informativo("===============Mis solicitudes===============");
        List<Solicitud> solicitudes = solicitudDao.listarPorCliente(idCliente());
        if (solicitudes.isEmpty()) {
            mensajes.informativo("No tiene solicitudes");
        } else {
            imprimirSolicitud.list(solicitudes);
        }
    }
}
