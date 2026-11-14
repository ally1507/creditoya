package examen;

import dao.ArchivoPrestamo;
import dao.IPagoDAO;
import dao.IPrestamoDAO;
import dao.PagoDAO;
import dao.PrestamoDAO;
import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import model.entity.Cuota;
import model.entity.EstadoPrestamo;
import model.entity.MetodoPago;
import model.entity.Pago;
import model.entity.Persona;
import model.entity.Prestamo;
import model.logica.CalculosCredito;
import model.logica.UsuarioSesion;
import view.Mensajes;
import view.Validaciones;
import view.pago.imprimir.ImprimirPago;
import view.prestamo.imprimir.ImprimirPrestamo;

/** Cls del examen: registro y control de pagos de prestamos (reutiliza lo de CrediYa) */
public class GestorPagos {

    /** Id del administrador inicial (crediya_db.sql), se usa si nadie inicio sesion */
    private static final int EMPLEADO_POR_DEFECTO = 1;

    private final Conexion conexion = new Conexion();
    private final IPrestamoDAO prestamoDao = new PrestamoDAO();
    private final IPagoDAO pagoDao = new PagoDAO();
    private final CalculosCredito calculos = new CalculosCredito();
    private final ImprimirPago imprimirPago = new ImprimirPago();
    private final ImprimirPrestamo imprimirPrestamo = new ImprimirPrestamo();
    private final Validaciones v = new Validaciones();
    private final Mensajes mensajes = new Mensajes();

    /** Mtdo para registrar un pago de un prestamo existente */
    public void registrarPago() {
        mensajes.informativo("===============Registrar pago (GestorPagos)===============");
        int prestamoId = v.validarEntero("Ingrese el id del prestamo");
        double monto = v.validarDecimal("Ingrese el monto pagado (sin puntos de miles)");
        LocalDate fecha = v.validarFecha("Ingrese la fecha del pago (AAAA-MM-DD)");
        try {
            if (fecha.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha del pago no puede ser futura");
            }
            Prestamo prestamo = buscarPrestamo(prestamoId);
            imprimirPrestamo.plan(prestamo);
            // Reparte el pago entre las cuotas y recalcula el estado (lanza IllegalArgumentException si no es valido)
            List<Cuota> abonadas = calculos.aplicarPago(prestamo, monto, fecha);
            Pago pago = new Pago(0, prestamo.getId(), empleadoActual(), fecha, monto,
                    MetodoPago.EFECTIVO, "Registrado desde GestorPagos");
            // Guarda el pago, actualiza las cuotas (saldo) y el estado del prestamo en MySQL
            if (pagoDao.registrar(pago, prestamo, abonadas)) {
                new ArchivoPrestamo().guardar(prestamoDao.listarConPlan());
                String estado = prestamo.getEstado() == EstadoPrestamo.PAGADO
                        ? "PAGADO (CANCELADO)" : prestamo.getEstado().name();
                mensajes.exito("""
                               Pago registrado con id %d
                               Saldo restante: $%,.2f
                               Estado del prestamo: %s""".formatted(pago.getId(),
                        prestamo.getSaldoPendiente(), estado));
            } else {
                mensajes.error("No fue posible registrar el pago");
            }
        } catch (PrestamoNoEncontradoException e) {
            mensajes.error(e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            mensajes.error(e.getMessage());
        }
    }

    /** Mtdo para mostrar el historial de pagos de un prestamo en orden cronologico */
    public void historial() {
        mensajes.informativo("===============Historial de pagos===============");
        int prestamoId = v.validarEntero("Ingrese el id del prestamo");
        try {
            buscarPrestamo(prestamoId);
            List<Pago> pagos = pagoDao.listarPorPrestamo(prestamoId).stream()
                    .sorted(Comparator.comparing(Pago::getFechaPago).thenComparing(Pago::getId))
                    .toList();
            mostrar(pagos, "El prestamo no tiene pagos");
        } catch (PrestamoNoEncontradoException | IllegalStateException e) {
            mensajes.error(e.getMessage());
        }
    }

    /** Mtdo para filtrar los pagos de un prestamo entre dos fechas (Stream API) */
    public List<Pago> pagosEnRango(int prestamoId, LocalDate desde, LocalDate hasta) {
        return pagoDao.listarPorPrestamo(prestamoId).stream()
                .filter(p -> !p.getFechaPago().isBefore(desde) && !p.getFechaPago().isAfter(hasta))
                .sorted(Comparator.comparing(Pago::getFechaPago))
                .toList();
    }

    /** Mtdo para ver los pagos entre las fechas que escriba el usuario */
    public void historialPorFechas() {
        mensajes.informativo("===============Pagos entre fechas===============");
        int prestamoId = v.validarEntero("Ingrese el id del prestamo");
        LocalDate desde = v.validarFecha("Fecha inicial (AAAA-MM-DD)");
        LocalDate hasta = v.validarFecha("Fecha final (AAAA-MM-DD)");
        try {
            if (hasta.isBefore(desde)) {
                throw new IllegalArgumentException("La fecha final no puede ser anterior a la inicial");
            }
            buscarPrestamo(prestamoId);
            mostrar(pagosEnRango(prestamoId, desde, hasta), "No hay pagos en ese rango de fechas");
        } catch (PrestamoNoEncontradoException | IllegalArgumentException | IllegalStateException e) {
            mensajes.error(e.getMessage());
        }
    }

    /** Mtdo para ver los pagos del ultimo mes */
    public void pagosUltimoMes() {
        mensajes.informativo("===============Pagos del ultimo mes===============");
        int prestamoId = v.validarEntero("Ingrese el id del prestamo");
        LocalDate hoy = LocalDate.now();
        try {
            buscarPrestamo(prestamoId);
            mostrar(pagosEnRango(prestamoId, hoy.minusMonths(1), hoy), "No hay pagos en el ultimo mes");
        } catch (PrestamoNoEncontradoException | IllegalStateException e) {
            mensajes.error(e.getMessage());
        }
    }

    /** Mtdo que verifica que el prestamo exista y lo trae con sus cuotas */
    public Prestamo buscarPrestamo(int prestamoId) throws PrestamoNoEncontradoException {
        if (!existePrestamo(prestamoId)) {
            throw new PrestamoNoEncontradoException(prestamoId);
        }
        Prestamo prestamo = prestamoDao.buscar(prestamoId);
        if (prestamo == null) {
            throw new PrestamoNoEncontradoException(prestamoId);
        }
        return prestamo;
    }

    /** Mtdo JDBC con try-catch-finally: pregunta a MySQL si existe el prestamo */
    private boolean existePrestamo(int prestamoId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conexion.conectar();
            ps = con.prepareStatement("select 1 from prestamos where id=?");
            ps.setInt(1, prestamoId);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            throw new IllegalStateException("No fue posible consultar el prestamo en la base de datos: "
                    + ex.getMessage(), ex);
        } finally {
            cerrar(rs, ps, con);
        }
    }

    /** Mtdo para cerrar las conexiones JDBC (en el finally) */
    private void cerrar(AutoCloseable... recursos) {
        for (AutoCloseable r : recursos) {
            if (r != null) {
                try {
                    r.close();
                } catch (Exception e) {
                    System.out.println("Error al cerrar la conexion!\n" + e.getMessage());
                }
            }
        }
    }

    /** Mtdo para imprimir pagos o avisar si no hay */
    private void mostrar(List<Pago> pagos, String siVacio) {
        if (pagos.isEmpty()) {
            mensajes.informativo(siVacio);
        } else {
            imprimirPago.list(pagos);
        }
    }

    /** Mtdo que devuelve el id del empleado en sesion (o el admin inicial) */
    private int empleadoActual() {
        Persona usuario = UsuarioSesion.getInstance().getUsuario();
        return usuario != null ? usuario.getId() : EMPLEADO_POR_DEFECTO;
    }

    /** Mtdo del menu del gestor de pagos */
    public void menu() {
        int op;
        do {
            op = v.validarEntero("""
                                 ===== Gestor de pagos =====
                                 1- Registrar pago.
                                 2- Historial de pagos de un prestamo.
                                 3- Pagos entre fechas.
                                 4- Pagos del ultimo mes.
                                 5- Salir.
                                 """);
            switch (op) {
                case 1 -> registrarPago();
                case 2 -> historial();
                case 3 -> historialPorFechas();
                case 4 -> pagosUltimoMes();
                case 5 -> mensajes.informativo("Saliendo del gestor de pagos...");
                default -> mensajes.error("La opcion esta fuera de rango");
            }
        } while (op != 5);
    }
}
