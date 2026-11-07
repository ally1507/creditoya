package model.entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Una cuota del plan de pagos de un prestamo.
 * valor = capital + interes. saldo = capital que queda despues de esta cuota.
 * abonado = lo que el cliente ya pago de esta cuota.
 */
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

    /** Lo que falta por pagar de esta cuota (redondeado a centavos). */
    public double getPendiente() {
        return Math.round((valor - abonado) * 100) / 100.0;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public double getCapital() {
        return capital;
    }

    public double getInteres() {
        return interes;
    }

    public double getValor() {
        return valor;
    }

    public double getAbonado() {
        return abonado;
    }

    public void setAbonado(double abonado) {
        this.abonado = abonado;
    }

    public double getSaldo() {
        return saldo;
    }

    public EstadoCuota getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuota estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "%3d | %s | %,14.2f | %,12.2f | %,14.2f | %,14.2f | %,14.2f | %s".formatted(
                numero, fechaVencimiento, capital, interes, valor, abonado, saldo, estado);
    }
}
