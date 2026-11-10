package model.entity;

import java.io.Serializable;
import java.time.LocalDate;

/** solicitud de credito */
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

    /** Cnstrc para una solicitud nueva PENDIENTE, fecha de hoy */
    public Solicitud(int clienteId, int empleadoId, double monto, int cuotas, String observaciones) {
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.cuotas = cuotas;
        this.observaciones = observaciones;
        this.fechaSolicitud = LocalDate.now();
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    /** Cnstrc para una solicitud con todos los datos */
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

    /** Mtdo que devuelve el id */
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

    /** Mtdo para cambiar las observaciones */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    /** Mtdo para mostrar la solicitud */
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
