package model.entity;

import java.io.Serializable;
import java.time.LocalDate;

/** Pago de un cliente a un prestamo, registrado por un empleado. */
public class Pago implements Serializable {

    private int id;
    private int prestamoId;
    private int empleadoId;
    private LocalDate fechaPago;
    private double monto;
    private MetodoPago metodo;
    private String observaciones;

    public Pago(int prestamoId, int empleadoId, double monto, MetodoPago metodo, String observaciones) {
        this.prestamoId = prestamoId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.metodo = metodo;
        this.observaciones = observaciones;
        this.fechaPago = LocalDate.now();
    }

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
