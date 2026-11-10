package controller;

import dao.ArchivoPrestamo;
import dao.IPagoDAO;
import dao.IPrestamoDAO;
import dao.PagoDAO;
import dao.PrestamoDAO;
import java.time.LocalDate;
import java.util.List;
import model.entity.Cuota;
import model.entity.EstadoPrestamo;
import model.entity.Pago;
import model.entity.Prestamo;
import model.logica.CalculosCredito;
import model.logica.UsuarioSesion;
import view.Mensajes;
import view.pago.imprimir.ImprimirPago;
import view.pago.imprimir.MenuPago;
import view.pago.ingreso.IngresoPago;
import view.prestamo.imprimir.ImprimirPrestamo;

/** Cls del modulo de pagos */
public class PagoController {

    private final MenuPago menu = new MenuPago();
    private final ImprimirPago imprimir = new ImprimirPago();
    private final ImprimirPrestamo imprimirPrestamo = new ImprimirPrestamo();
    private final IngresoPago ingreso = new IngresoPago();
    private final IPagoDAO dao = new PagoDAO();
    private final IPrestamoDAO prestamoDao = new PrestamoDAO();
    private final CalculosCredito calculos = new CalculosCredito();
    private final Mensajes mensajes = new Mensajes();

    /** Mtdo para registrar un pago */
    private void registrar() {
        mensajes.informativo("===============Modulo registrar pago===============");
        Prestamo prestamo = prestamoDao.buscar(ingreso.pedirIdPrestamo());
        if (prestamo == null) {
            mensajes.error("El prestamo no existe");
            return;
        }
        imprimirPrestamo.plan(prestamo);
        if (prestamo.getEstado() == EstadoPrestamo.PAGADO) {
            mensajes.error("El prestamo ya esta pagado");
            return;
        }
        int empleadoId = UsuarioSesion.getInstance().getUsuario().getId();
        Pago pago = new Pago(prestamo.getId(), empleadoId, ingreso.pedirMonto(), ingreso.pedirMetodo(),
                ingreso.pedirObservaciones());
        try {
            List<Cuota> abonadas = calculos.aplicarPago(prestamo, pago.getMonto(), LocalDate.now());
            if (ingreso.confirmar("¿Registrar pago de $%,.2f al prestamo %d?".formatted(pago.getMonto(), prestamo.getId())) != 0) {
                mensajes.informativo("Pago cancelado!");
                return;
            }
            if (dao.registrar(pago, prestamo, abonadas)) {
                new ArchivoPrestamo().guardar(prestamoDao.listarConPlan());
                mensajes.exito("""
                               Pago registrado con id %d
                               Cuotas abonadas: %d
                               Saldo pendiente: $%,.2f
                               Estado del prestamo: %s""".formatted(pago.getId(), abonadas.size(),
                        prestamo.getSaldoPendiente(), prestamo.getEstado()));
            } else {
                mensajes.error("No fue posible registrar el pago");
            }
        } catch (IllegalArgumentException e) {
            mensajes.error(e.getMessage());
        }
    }

    /** Mtdo para ver los pagos de un prestamo */
    private void porPrestamo() {
        mensajes.informativo("===============Modulo pagos de un prestamo===============");
        mostrar(dao.listarPorPrestamo(ingreso.pedirIdPrestamo()), "El prestamo no tiene pagos");
    }

    /** Mtdo para ver los pagos entre dos fechas */
    private void entreFechas() {
        mensajes.informativo("===============Modulo pagos entre fechas===============");
        LocalDate desde = ingreso.pedirFecha("Fecha inicial");
        LocalDate hasta = ingreso.pedirFecha("Fecha final");
        if (hasta.isBefore(desde)) {
            mensajes.error("La fecha final no puede ser anterior a la inicial");
        } else {
            mostrar(dao.listarPorFechas(desde, hasta), "No hay pagos en ese rango de fechas");
        }
    }

    /** Mtdo para imprimir pagos o avisar si no hay */
    private void mostrar(List<Pago> pagos, String siVacio) {
        if (pagos.isEmpty()) {
            mensajes.informativo(siVacio);
        } else {
            imprimir.list(pagos);
        }
    }

    /** Mtdo del menu de pagos */
    public void menu() {
        int op;
        do {
            op = menu.menu();
            switch (op) {
                case 1 -> registrar();
                case 2 -> porPrestamo();
                case 3 -> entreFechas();
                case 4 -> mensajes.informativo("Regresando al menu principal...");
                default -> mensajes.error("La opcion esta fuera de rango");
            }
        } while (op != 4);
    }
}
