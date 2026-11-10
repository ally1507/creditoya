package model.entity;

import java.io.Serializable;
import java.time.LocalDate;

/** pago de un prestamo */
public class Pago implements Serializable {

    private int id;
    private int prestamoId;
    private int empleadoId;
    private LocalDate fechaPago;
    private double monto;
    private MetodoPago metodo;
    private String observaciones;

    /** Cnstrc para un pago nuevo con fecha de hoy */
    public Pago(int prestamoId, int empleadoId, double monto, MetodoPago metodo, String observaciones) {
        this.prestamoId = prestamoId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.metodo = metodo;
        this.observaciones = observaciones;
        this.fechaPago = LocalDate.now();
    }

    /** Cnstrc para un pago con todos los datos */
    public Pago(int id, int prestamoId, int empleadoId, LocalDate fechaPago, double monto,
            MetodoPago metodo, String observaciones) {
        this.id = id;
        this.prestamoId = prestamoId;
        this.empleadoId = empleadoId;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.metodo = metodo;
        this.observaciones = observaciones;
    }

    /** devuelve el id */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrestamoId() {
        return prestamoId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    /** d la fecha del pago */
    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    public MetodoPago getMetodo() {
        return metodo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    /** Mtdo para mostrar el pago */
    @Override
    public String toString() {
        return """
               ********************
               Pago:           %s
               Prestamo:       %s
               Fecha:          %s
               Monto:          $%,.2f
               Metodo:         %s
               Observaciones:  %s
               """.formatted(id, prestamoId, fechaPago, monto, metodo,
                observaciones == null ? "-" : observaciones);
    }
}
