package model.entity;

import java.io.Serializable;
import java.time.LocalDate;

/** Cls de una cuota del plan de pagos */
public class Cuota implements Serializable {

    private int id;
    private int numero;
    private LocalDate fechaVencimiento;
    private double capital;
    private double interes;
    private double valor;
    private double abonado;
    private double saldo;
    private EstadoCuota estado;

    /** Cnstrc para una cuota con todos los datos */
    public Cuota(int id, int numero, LocalDate fechaVencimiento, double capital,
            double interes, double valor, double abonado, double saldo, EstadoCuota estado) {
        this.id = id;
        this.numero = numero;
        this.fechaVencimiento = fechaVencimiento;
        this.capital = capital;
        this.interes = interes;
        this.valor = valor;
        this.abonado = abonado;
        this.saldo = saldo;
        this.estado = estado;
    }

    /** Mtdo para calcular lo que falta de la cuota */
    public double getPendiente() {
        return Math.round((valor - abonado) * 100) / 100.0;
    }

    /** d el id */
    public int getId() {
        return id;
    }

    /** d el numero de cuota */
    public int getNumero() {
        return numero;
    }

    /** d vencimiento */
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    /** d el capital */
    public double getCapital() {
        return capital;
    }

    /** d el interes */
    public double getInteres() {
        return interes;
    }

    /** d el valor de la cuota */
    public double getValor() {
        return valor;
    }

    /** d lo abonado */
    public double getAbonado() {
        return abonado;
    }

    /** M para cambiar lo abonado */
    public void setAbonado(double abonado) {
        this.abonado = abonado;
    }

    /** d el saldo de capital */
    public double getSaldo() {
        return saldo;
    }

    /** d el estado */
    public EstadoCuota getEstado() {
        return estado;
    }

    /** cambiar el estado */
    public void setEstado(EstadoCuota estado) {
        this.estado = estado;
    }

    /** mostrar la cuota como fila */
    @Override
    public String toString() {
        return "%3d | %s | %,14.2f | %,12.2f | %,14.2f | %,14.2f | %,14.2f | %s".formatted(
                numero, fechaVencimiento, capital, interes, valor, abonado, saldo, estado);
    }
}
