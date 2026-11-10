package model.entity;

import java.time.LocalDate;

/** Cls de una cuota vencida (reporte de mora) */
public class CuotaEnMora {

    private int prestamoId;
    private String cliente;
    private String documento;
    private String telefono;
    private int numeroCuota;
    private LocalDate vencimiento;
    private double pendiente;
    private int diasMora;

    /** Cnstrc para una fila del reporte de mora */
    public CuotaEnMora(int prestamoId, String cliente, String documento, String telefono, int numeroCuota,
            LocalDate vencimiento, double pendiente, int diasMora) {
        this.prestamoId = prestamoId;
        this.cliente = cliente;
        this.documento = documento;
        this.telefono = telefono;
        this.numeroCuota = numeroCuota;
        this.vencimiento = vencimiento;
        this.pendiente = pendiente;
        this.diasMora = diasMora;
    }

    /** d el id del prestamo */
    public int getPrestamoId() {
        return prestamoId;
    }

    /** d lo pendiente */
    public double getPendiente() {
        return pendiente;
    }

    /**  para mostrar la fila de mora */
    @Override
    public String toString() {
        return "%8d | %-25s | %-12s | %-10s | %5d | %s | %,14.2f | %5d".formatted(prestamoId, cliente,
                documento, telefono == null ? "-" : telefono, numeroCuota, vencimiento, pendiente, diasMora);
    }
}
