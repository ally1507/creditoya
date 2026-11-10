package model.entity;

/** fila del reporte de cartera */
public class ResumenCartera {

    private String estado;
    private int cantidad;
    private double montoPrestado;
    private double saldoPendiente;

    /** Cnstrc para una fila de cartera */
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

    /** Mtdo para mostrar la fila de cartera */
    @Override
    public String toString() {
        return "%-10s | %8d | %,18.2f | %,18.2f".formatted(estado, cantidad, montoPrestado, saldoPendiente);
    }
}
