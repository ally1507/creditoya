package controller;

import dao.ArchivoPrestamo;
import dao.ClienteDAO;
import dao.IClienteDAO;
import dao.IPrestamoDAO;
import dao.PrestamoDAO;
import java.util.List;
import model.entity.Cliente;
import model.entity.EstadoPrestamo;
import model.entity.Prestamo;
import view.Mensajes;
import view.prestamo.imprimir.ImprimirPrestamo;
import view.prestamo.imprimir.MenuPrestamo;
import view.prestamo.ingreso.IngresoPrestamo;

/** Cls del modulo de prestamos */
public class PrestamoController {

    private final MenuPrestamo menu = new MenuPrestamo();
    private final ImprimirPrestamo imprimir = new ImprimirPrestamo();
    private final IngresoPrestamo ingreso = new IngresoPrestamo();
    private final IPrestamoDAO dao = new PrestamoDAO();
    private final IClienteDAO clienteDao = new ClienteDAO();
    private final ArchivoPrestamo archivo = new ArchivoPrestamo();
    private final Mensajes mensajes = new Mensajes();

    /** Mtdo para ver un prestamo con sus cuotas */
    private void verPlan() {
        mensajes.informativo("===============Modulo plan de cuotas===============");
        Prestamo p = dao.buscar(ingreso.pedirId());
        if (p == null) {
            mensajes.error("El prestamo no existe");
        } else {
            imprimir.plan(p);
        }
    }

    /** Mtdo para traer los prestamos de un cliente */
    private void porCliente() {
        mensajes.informativo("===============Modulo prestamos de un cliente===============");
        Cliente c = clienteDao.buscarPorDocumento(ingreso.pedirDocumentoCliente());
        if (c == null) {
            mensajes.error("El cliente no existe");
        } else {
            mostrar(dao.listarPorCliente(c.getId()), "El cliente " + c.getNombre() + " no tiene prestamos");
        }
    }

    /** Mtdo para traer los prestamos por estado */
    private void porEstado() {
        mensajes.informativo("===============Modulo prestamos por estado===============");
        mostrar(dao.listarPorEstado(ingreso.pedirEstado()), "No hay prestamos en ese estado");
    }

    /** Mtdo para cambiar el estado a mano */
    private void cambiarEstado() {
        mensajes.informativo("===============Modulo cambiar estado===============");
        Prestamo p = dao.buscar(ingreso.pedirId());
        if (p == null) {
            mensajes.error("El prestamo no existe");
            return;
        }
        imprimir.entity(p);
        EstadoPrestamo nuevo = ingreso.pedirNuevoEstado();
        if (nuevo == p.getEstado()) {
            mensajes.informativo("El prestamo ya esta en estado " + nuevo);
            return;
        }
        if (nuevo == EstadoPrestamo.PAGADO && p.getSaldoPendiente() > 0
                && ingreso.confirmar("El prestamo aun debe $%,.2f. Marcarlo como PAGADO?".formatted(p.getSaldoPendiente())) != 0) {
            mensajes.informativo("Operacion cancelada!");
            return;
        }
        if (dao.actualizarEstado(p.getId(), nuevo)) {
            archivo.guardar(dao.listarConPlan());
            mensajes.exito("Estado del prestamo " + p.getId() + " cambiado a " + nuevo);
        } else {
            mensajes.error("No fue posible cambiar el estado");
        }
    }

    /** Mtdo para imprimir prestamos o avisar si no hay */
    private void mostrar(List<Prestamo> prestamos, String siVacio) {
        if (prestamos.isEmpty()) {
            mensajes.informativo(siVacio);
        } else {
            imprimir.list(prestamos);
        }
    }

    /** Mtdo del menu de prestamos */
    public void menu() {
        int op;
        do {
            op = menu.menu();
            switch (op) {
                case 1 -> verPlan();
                case 2 -> porCliente();
                case 3 -> porEstado();
                case 4 -> mostrar(dao.listar(), "No existen prestamos aun");
                case 5 -> cambiarEstado();
                case 6 -> {
                    mensajes.informativo("===============Prestamos guardados en archivo===============");
                    mostrar(archivo.listar(), "El archivo de prestamos esta vacio");
                }
                case 7 -> mensajes.informativo("Regresando al menu principal...");
                default -> mensajes.error("La opcion esta fuera de rango");
            }
        } while (op != 7);
    }
}
