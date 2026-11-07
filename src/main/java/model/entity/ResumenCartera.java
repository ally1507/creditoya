package model.entity;

/** Una fila del reporte de cartera: prestamos de un estado y sus totales. */
public class ResumenCartera {

    private String estado;
    private int cantidad;
    private double montoPrestado;
    private double saldoPendiente;

    public ResumenCartera(String estado, int cantidad, double montoPrestado, double saldoPendiente) {
        this.estado = estado;
        this.cantidad = cantidad;
        this.montoPrestado = montoPrestado;
        this.saldoPendiente = saldoPendiente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getMontoPrestado() {
        return montoPrestado;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    @Override
    public String toString() {
        return "%-10s | %8d | %,18.2f | %,18.2f".formatted(estado, cantidad, montoPrestado, saldoPendiente);
    }
}
