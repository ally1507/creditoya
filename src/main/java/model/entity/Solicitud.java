package model.entity;

import java.io.Serializable;
import java.time.LocalDate;

/** Solicitud de credito de un cliente, registrada por un empleado. */
public class Solicitud implements Serializable {

    private int id;
    private int clienteId;
    private int empleadoId;
    private double monto;
    private int cuotas;
    private LocalDate fechaSolicitud;
    private EstadoSolicitud estado;
    private String observaciones;
    private String nombreCliente;

    public Solicitud(int clienteId, int empleadoId, double monto, int cuotas, String observaciones) {
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.cuotas = cuotas;
        this.observaciones = observaciones;
        this.fechaSolicitud = LocalDate.now();
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public Solicitud(int id, int clienteId, int empleadoId, double monto, int cuotas,
            LocalDate fechaSolicitud, EstadoSolicitud estado, String observaciones, String nombreCliente) {
        this.id = id;
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.cuotas = cuotas;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.observaciones = observaciones;
        this.nombreCliente = nombreCliente;
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

    public double getMonto() {
        return monto;
    }

    public int getCuotas() {
        return cuotas;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    @Override
    public String toString() {
        return """
               ********************
               Solicitud:      %s
               Cliente:        %s
               Monto:          $%,.2f
               Cuotas:         %s
               Fecha:          %s
               Estado:         %s
               Observaciones:  %s
               """.formatted(id, nombreCliente, monto, cuotas, fechaSolicitud, estado,
                observaciones == null ? "-" : observaciones);
    }
}
