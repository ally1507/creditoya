package model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Prestamo creado al aprobar una solicitud. Tiene una lista de cuotas
 * (relacion entre clases: un prestamo tiene muchas cuotas).
 * Las tasas estan en porcentaje (24 = 24% efectivo anual).
 */
public class Prestamo implements Serializable {

    private int id;
    private int clienteId;
    private int empleadoId;
    private int solicitudId;
    private double monto;
    private double tasaEa;
    private double tasaMensual;
    private int cuotas;
    private double totalPagar;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoPrestamo estado;
    private String nombreCliente;
    private List<Cuota> plan = new ArrayList<>();

    public Prestamo(int id, int clienteId, int empleadoId, int solicitudId, double monto, double tasaEa,
            double tasaMensual, int cuotas, double totalPagar, LocalDate fechaInicio, LocalDate fechaFin,
            EstadoPrestamo estado, String nombreCliente) {
        this.id = id;
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.solicitudId = solicitudId;
        this.monto = monto;
        this.tasaEa = tasaEa;
        this.tasaMensual = tasaMensual;
        this.cuotas = cuotas;
        this.totalPagar = totalPagar;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.nombreCliente = nombreCliente;
    }

    /** Suma de lo que falta por pagar de todas las cuotas. */
    public double getSaldoPendiente() {
        double total = 0;
        for (Cuota c : plan) {
            total += c.getPendiente();
        }
        return Math.round(total * 100) / 100.0;
    }

    /** Suma de lo que el cliente ya abono. */
    public double getTotalAbonado() {
        double total = 0;
        for (Cuota c : plan) {
            total += c.getAbonado();
        }
        return Math.round(total * 100) / 100.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public int getSolicitudId() {
        return solicitudId;
    }

    public double getMonto() {
        return monto;
    }

    public double getTasaEa() {
        return tasaEa;
    }

    public double getTasaMensual() {
        return tasaMensual;
    }

    public int getCuotas() {
        return cuotas;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public List<Cuota> getPlan() {
        return plan;
    }

    public void setPlan(List<Cuota> plan) {
        this.plan = plan;
    }

    @Override
    public String toString() {
        return """
               ********************
               Prestamo:       %s  (solicitud %s)
               Cliente:        %s
               Monto:          $%,.2f
               Tasa:           %s%% E.A. (%s%% mensual)
               Cuotas:         %s
               Total a pagar:  $%,.2f
               Inicio / fin:   %s / %s
               Estado:         %s
               """.formatted(id, solicitudId, nombreCliente, monto, tasaEa, tasaMensual, cuotas,
                totalPagar, fechaInicio, fechaFin, estado);
    }
}
