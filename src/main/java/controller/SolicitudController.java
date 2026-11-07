package controller;

import dao.ArchivoPrestamo;
import dao.ClienteDAO;
import dao.IClienteDAO;
import dao.IPrestamoDAO;
import dao.ISolicitudDAO;
import dao.PrestamoDAO;
import dao.SolicitudDAO;
import java.time.LocalDate;
import java.util.List;
import model.entity.Cliente;
import model.entity.EstadoSolicitud;
import model.entity.Prestamo;
import model.entity.Solicitud;
import model.logica.CalculosCredito;
import model.logica.UsuarioSesion;
import view.Mensajes;
import view.prestamo.imprimir.ImprimirPrestamo;
import view.solicitud.imprimir.ImprimirSolicitud;
import view.solicitud.imprimir.MenuSolicitud;
import view.solicitud.ingreso.IngresoSolicitud;

/** Registro y estudio de solicitudes. Al aprobar una se crea el prestamo. */
public class SolicitudController {

    private final MenuSolicitud menu = new MenuSolicitud();
    private final ImprimirSolicitud imprimir = new ImprimirSolicitud();
    private final ImprimirPrestamo imprimirPrestamo = new ImprimirPrestamo();
    private final IngresoSolicitud ingreso = new IngresoSolicitud();
    private final ISolicitudDAO dao = new SolicitudDAO();
    private final IClienteDAO clienteDao = new ClienteDAO();
    private final IPrestamoDAO prestamoDao = new PrestamoDAO();
    private final CalculosCredito calculos = new CalculosCredito();
    private final Mensajes mensajes = new Mensajes();

    private void registrar() {
        mensajes.informativo("===============Modulo registrar solicitud===============");
        Cliente cliente = clienteDao.buscarPorDocumento(ingreso.pedirDocumentoCliente());
        if (cliente == null) {
            mensajes.error("No existe un cliente con ese documento. Registrelo primero en el modulo Clientes");
            return;
        }
        if (!cliente.isActivo()) {
            mensajes.error("El cliente esta inactivo y no puede solicitar creditos");
            return;
        }
        mensajes.informativo("Cliente: " + cliente.getNombre());
        int empleadoId = UsuarioSesion.getInstance().getUsuario().getId();
        Solicitud s = new Solicitud(cliente.getId(), empleadoId, ingreso.pedirMonto(),
                ingreso.pedirCuotas(), ingreso.pedirObservaciones());
        if (dao.agregar(s)) {
            mensajes.exito("Solicitud registrada con id " + s.getId() + " (estado: PENDIENTE)");
        } else {
            mensajes.error("No fue posible registrar la solicitud");
        }
    }

    private boolean listarPendientes() {
        List<Solicitud> pendientes = dao.listarPorEstado(EstadoSolicitud.PENDIENTE);
        if (pendientes.isEmpty()) {
            mensajes.informativo("No hay solicitudes pendientes");
            return false;
        }
        imprimir.list(pendientes);
        return true;
    }

    /** Busca una solicitud y verifica que siga pendiente. */
    private Solicitud pedirPendiente() {
        Solicitud s = dao.buscar(ingreso.pedirId());
        if (s == null) {
            mensajes.error("La solicitud no existe");
            return null;
        }
        if (s.getEstado() != EstadoSolicitud.PENDIENTE) {
            mensajes.error("La solicitud ya fue " + s.getEstado());
            return null;
        }
        return s;
    }

    private void aprobar() {
        mensajes.informativo("===============Modulo aprobar solicitud===============");
        if (!listarPendientes()) {
            return;
        }
        Solicitud s = pedirPendiente();
        if (s == null) {
            return;
        }
        double tasaEa = ingreso.pedirTasaEa();
        int empleadoId = UsuarioSesion.getInstance().getUsuario().getId();
        try {
            Prestamo prestamo = calculos.crearPrestamo(s, tasaEa, empleadoId, LocalDate.now());
            mensajes.informativo("Vista previa del prestamo:");
            imprimirPrestamo.plan(prestamo);
            if (ingreso.confirmar("¿Aprobar la solicitud " + s.getId() + " y desembolsar el prestamo?") != 0) {
                mensajes.informativo("Aprobacion cancelada!");
                return;
            }
            s.setObservaciones(ingreso.pedirObservaciones());
            if (prestamoDao.aprobar(s, prestamo)) {
                new ArchivoPrestamo().guardar(prestamoDao.listarConPlan());
                mensajes.exito("Solicitud aprobada. Prestamo creado con id " + prestamo.getId());
            } else {
                mensajes.error("No fue posible aprobar la solicitud");
            }
        } catch (IllegalArgumentException e) {
            mensajes.error(e.getMessage());
        }
    }

    private void rechazar() {
        mensajes.informativo("===============Modulo rechazar solicitud===============");
        if (!listarPendientes()) {
            return;
        }
        Solicitud s = pedirPendiente();
        if (s == null) {
            return;
        }
        s.setObservaciones(ingreso.pedirMotivoRechazo());
        if (ingreso.confirmar("¿Rechazar la solicitud " + s.getId() + "?") != 0) {
            mensajes.informativo("Operacion cancelada!");
        } else if (dao.rechazar(s)) {
            mensajes.exito("Solicitud rechazada");
        } else {
            mensajes.error("No fue posible rechazar la solicitud (puede que ya no este pendiente)");
        }
    }

    private void buscar() {
        mensajes.informativo("===============Modulo buscar solicitud===============");
        Solicitud s = dao.buscar(ingreso.pedirId());
        if (s == null) {
            mensajes.error("La solicitud no existe");
        } else {
            imprimir.entity(s);
        }
    }

    private void listar() {
        List<Solicitud> solicitudes = dao.listar();
        if (solicitudes.isEmpty()) {
            mensajes.informativo("No existen solicitudes aun");
        } else {
            imprimir.list(solicitudes);
        }
    }

    public void menu() {
        int op;
        do {
            op = menu.menu();
            switch (op) {
                case 1 -> registrar();
                case 2 -> listarPendientes();
                case 3 -> aprobar();
                case 4 -> rechazar();
                case 5 -> buscar();
                case 6 -> listar();
                case 7 -> mensajes.informativo("Regresando al menu principal...");
                default -> mensajes.error("La opcion esta fuera de rango");
            }
        } while (op != 7);
    }
}
