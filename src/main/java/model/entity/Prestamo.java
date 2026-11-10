package model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** prestamo con su lista de cuotas */
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

    /** un prestamo con todos los datos */
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

    /** Mtdo para calcular lo que falta por pagar */
    public double getSaldoPendiente() {
        double total = 0;
        for (Cuota c : plan) {
            total += c.getPendiente();
        }
        return Math.round(total * 100) / 100.0;
    }

    /** Mtdo para calcular lo que ya se pago */
    public double getTotalAbonado() {
        double total = 0;
        for (Cuota c : plan) {
            total += c.getAbonado();
        }
        return Math.round(total * 100) / 100.0;
    }

    /** Mtdo que devuelve el id */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** d el id del cliente */
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

    /** Mtdo para mostrar el prestamo */
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
