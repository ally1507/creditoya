package controller;

import dao.IPagoDAO;
import dao.IPrestamoDAO;
import dao.ISolicitudDAO;
import dao.PagoDAO;
import dao.PrestamoDAO;
import dao.ReporteDAO;
import dao.SolicitudDAO;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import model.entity.CuotaEnMora;
import model.entity.EstadoPrestamo;
import model.entity.EstadoSolicitud;
import model.entity.Pago;
import model.entity.Prestamo;
import model.entity.ResumenCartera;
import model.entity.Solicitud;
import view.Mensajes;
import view.pago.ingreso.IngresoPago;
import view.prestamo.imprimir.ImprimirPrestamo;
import view.reporte.imprimir.ImprimirReporte;
import view.reporte.imprimir.MenuReporte;

/** Cls del modulo de reportes */
public class ReporteController {

    private final MenuReporte menu = new MenuReporte();
    private final ImprimirReporte imprimir = new ImprimirReporte();
    private final IngresoPago ingresoFechas = new IngresoPago();
    private final ReporteDAO dao = new ReporteDAO();
    private final IPagoDAO pagoDao = new PagoDAO();
    private final ISolicitudDAO solicitudDao = new SolicitudDAO();
    private final IPrestamoDAO prestamoDao = new PrestamoDAO();
    private final ImprimirPrestamo imprimirPrestamo = new ImprimirPrestamo();

    // Filtros Lambda (Predicate)
    private final Predicate<Prestamo> activo = p -> p.getEstado() == EstadoPrestamo.ACTIVO;
    private final Predicate<Prestamo> vencido = p -> p.getEstado() == EstadoPrestamo.VENCIDO;
    private final Mensajes mensajes = new Mensajes();

    /** Mtdo del resumen de cartera por estado */
    private void cartera() {
        mensajes.informativo("===============Resumen de cartera===============");
        List<ResumenCartera> filas = dao.resumenCartera();
        if (filas.isEmpty()) {
            mensajes.informativo("Aun no hay prestamos");
        } else {
            imprimir.cartera(filas);
        }
    }

    /** Mtdo para ver las cuotas en mora */
    private void mora() {
        mensajes.informativo("===============Cuotas en mora===============");
        List<CuotaEnMora> filas = dao.cuotasEnMora();
        if (filas.isEmpty()) {
            mensajes.informativo("No hay cuotas vencidas. Cartera al dia!");
        } else {
            imprimir.mora(filas);
        }
    }

    /** Mtdo para sumar lo recaudado por metodo de pago */
    private void recaudo() {
        mensajes.informativo("===============Recaudo entre fechas===============");
        LocalDate desde = ingresoFechas.pedirFecha("Fecha inicial");
        LocalDate hasta = ingresoFechas.pedirFecha("Fecha final");
        if (hasta.isBefore(desde)) {
            mensajes.error("La fecha final no puede ser anterior a la inicial");
            return;
        }
        List<Pago> pagos = pagoDao.listarPorFechas(desde, hasta);
        if (pagos.isEmpty()) {
            mensajes.informativo("No hay pagos en ese rango de fechas");
            return;
        }
        Map<String, Double> porMetodo = new HashMap<>();
        for (Pago p : pagos) {
            String metodo = p.getMetodo().name();
            if (porMetodo.containsKey(metodo)) {
                porMetodo.replace(metodo, porMetodo.get(metodo) + p.getMonto());
            } else {
                porMetodo.put(metodo, p.getMonto());
            }
        }
        imprimir.recaudo("Recaudo del " + desde + " al " + hasta + " (" + pagos.size() + " pagos):", porMetodo);
    }

    /** Mtdo para contar solicitudes por estado */
    private void solicitudes() {
        mensajes.informativo("===============Solicitudes por estado===============");
        List<Solicitud> lista = solicitudDao.listar();
        if (lista.isEmpty()) {
            mensajes.informativo("Aun no hay solicitudes");
            return;
        }
        Map<String, Long> conteo = new HashMap<>();
        for (EstadoSolicitud estado : EstadoSolicitud.values()) {
            long cantidad = lista.stream().filter(s -> s.getEstado() == estado).count();
            conteo.put(estado.name(), cantidad);
        }
        imprimir.conteo("Cantidad de solicitudes:", conteo);
    }

    /** Mtdo para traer prestamos con un filtro */
    private void prestamosFiltrados(String titulo, Predicate<Prestamo> filtro, String siVacio) {
        mensajes.informativo("===============" + titulo + "===============");
        List<Prestamo> filtrados = prestamoDao.listar().stream().filter(filtro).toList();
        if (filtrados.isEmpty()) {
            mensajes.informativo(siVacio);
        } else {
            imprimirPrestamo.list(filtrados);
            mensajes.informativo("Total: " + filtrados.size() + " prestamos");
        }
    }

    /** Mtdo para traer los clientes morosos */
    private void clientesMorosos() {
        mensajes.informativo("===============Clientes morosos===============");
        List<String> morosos = prestamoDao.listar().stream()
                .filter(vencido)
                .map(p -> p.getNombreCliente())
                .distinct()
                .toList();
        if (morosos.isEmpty()) {
            mensajes.informativo("No hay clientes morosos");
        } else {
            morosos.forEach(nombre -> System.out.println("- " + nombre));
            mensajes.informativo("Total: " + morosos.size() + " clientes morosos");
        }
    }

    /** Mtdo del menu de reportes */
    public void menu() {
        int op;
        do {
            op = menu.menu();
            switch (op) {
                case 1 -> cartera();
                case 2 -> mora();
                case 3 -> recaudo();
                case 4 -> solicitudes();
                case 5 -> prestamosFiltrados("Prestamos activos", activo, "No hay prestamos activos");
                case 6 -> prestamosFiltrados("Prestamos vencidos", vencido, "No hay prestamos vencidos");
                case 7 -> clientesMorosos();
                case 8 -> mensajes.informativo("Regresando al menu principal...");
                default -> mensajes.error("La opcion esta fuera de rango");
            }
        } while (op != 8);
    }
}
