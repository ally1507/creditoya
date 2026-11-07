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

/** Lo que un cliente puede consultar de SU informacion al iniciar sesion. */
public class PortalClienteController {

    private final IPrestamoDAO prestamoDao = new PrestamoDAO();
    private final IPagoDAO pagoDao = new PagoDAO();
    private final ISolicitudDAO solicitudDao = new SolicitudDAO();
    private final ImprimirPrestamo imprimirPrestamo = new ImprimirPrestamo();
    private final ImprimirPago imprimirPago = new ImprimirPago();
    private final ImprimirSolicitud imprimirSolicitud = new ImprimirSolicitud();
    private final IngresoPrestamo ingreso = new IngresoPrestamo();
    private final Mensajes mensajes = new Mensajes();

    private int idCliente() {
        return UsuarioSesion.getInstance().getUsuario().getId();
    }

    public void misPrestamos() {
        mensajes.informativo("===============Mis prestamos===============");
        List<Prestamo> prestamos = prestamoDao.listarPorCliente(idCliente());
        if (prestamos.isEmpty()) {
            mensajes.informativo("No tiene prestamos");
        } else {
            imprimirPrestamo.list(prestamos);
        }
    }

    public void planDeCuotas() {
        mensajes.informativo("===============Plan de cuotas===============");
        Prestamo p = prestamoDao.buscar(ingreso.pedirId());
        // Si el prestamo es de otro cliente se responde igual que si no existiera.
        if (p == null || p.getClienteId() != idCliente()) {
            mensajes.error("No tiene un prestamo con ese id");
        } else {
            imprimirPrestamo.plan(p);
        }
    }

    public void misPagos() {
        mensajes.informativo("===============Mis pagos===============");
        List<Pago> pagos = pagoDao.listarPorCliente(idCliente());
        if (pagos.isEmpty()) {
            mensajes.informativo("No tiene pagos registrados");
        } else {
            imprimirPago.list(pagos);
        }
    }

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
