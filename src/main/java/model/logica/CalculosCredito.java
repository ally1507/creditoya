package model.logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.entity.Cuota;
import model.entity.EstadoCuota;
import model.entity.EstadoPrestamo;
import model.entity.Prestamo;
import model.entity.Solicitud;

/** Cls de calculos del credito (sistema frances) genera el plan y calcula cada fila */
public class CalculosCredito {

    /**  redondear a 2 decimales */
    public double redondear(double valor) {
        return Math.round(valor * 100) / 100.0;
    }

    /** pasar la tasa E.A. a mensual */
    public double calcularTasaMensual(double tasaEa) {
        double mensual = Math.pow(1 + tasaEa / 100, 1.0 / 12) - 1;
        return Math.round(mensual * 100 * 1000000) / 1000000.0;
    }

    /** calcular la cuota fija */
    public double calcularCuotaFija(double monto, double tasaMensual, int cuotas) {
        double i = tasaMensual / 100;
        if (i == 0) {
            return redondear(monto / cuotas);
        }
        return redondear(monto * i / (1 - Math.pow(1 + i, -cuotas)));
    }

    /**  armar el plan de cuotas  */
    public List<Cuota> generarPlan(double monto, double tasaMensual, int cuotas, LocalDate fechaInicio) {
        double i = tasaMensual / 100;
        double cuotaFija = calcularCuotaFija(monto, tasaMensual, cuotas);
        if (cuotaFija <= 0) {
            throw new IllegalArgumentException("El monto es muy bajo para ese numero de cuotas");
        }
        List<Cuota> plan = new ArrayList<>();
        double saldo = monto;
        for (int numero = 1; numero <= cuotas; numero++) {
            double interes = redondear(saldo * i);
            double capital = redondear(cuotaFija - interes);
            if (numero == cuotas || capital > saldo) {
                capital = saldo;
            }
            saldo = redondear(saldo - capital);
            plan.add(new Cuota(0, numero, fechaInicio.plusMonths(numero), capital, interes,
                    redondear(capital + interes), 0, saldo, EstadoCuota.PENDIENTE));
        }
        return plan;
    }

    /**a crear el prestamo de una solicitud */
    public Prestamo crearPrestamo(Solicitud s, double tasaEa, int empleadoId, LocalDate fechaInicio) {
        double tasaMensual = calcularTasaMensual(tasaEa);
        List<Cuota> plan = generarPlan(s.getMonto(), tasaMensual, s.getCuotas(), fechaInicio);
        double total = 0;
        for (Cuota c : plan) {
            total += c.getValor();
        }
        Prestamo p = new Prestamo(0, s.getClienteId(), empleadoId, s.getId(), s.getMonto(), tasaEa,
                tasaMensual, s.getCuotas(), redondear(total), fechaInicio,
                fechaInicio.plusMonths(s.getCuotas()), EstadoPrestamo.ACTIVO, s.getNombreCliente());
        p.setPlan(plan);
        return p;
    }

    /** repartir un pago entre las cuotas examen1 */
    public List<Cuota> aplicarPago(Prestamo p, double monto, LocalDate hoy) {
        if (p.getEstado() == EstadoPrestamo.PAGADO) {
            throw new IllegalArgumentException("El prestamo ya esta pagado");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor que 0");
        }
        if (monto > p.getSaldoPendiente()) {
            throw new IllegalArgumentException("El pago supera el saldo pendiente de $%,.2f"
                    .formatted(p.getSaldoPendiente()));
        }
        List<Cuota> abonadas = new ArrayList<>();
        double restante = monto;
        for (Cuota c : p.getPlan()) {
            if (restante <= 0) {
                break;
            }
            if (c.getEstado() == EstadoCuota.PAGADA) {
                continue;
            }
            double usado = Math.min(restante, c.getPendiente());
            c.setAbonado(redondear(c.getAbonado() + usado));
            restante = redondear(restante - usado);
            if (c.getPendiente() == 0) {
                c.setEstado(EstadoCuota.PAGADA);
            } else if (c.getFechaVencimiento().isBefore(hoy)) {
                c.setEstado(EstadoCuota.VENCIDA);
            } else {
                c.setEstado(EstadoCuota.PARCIAL);
            }
            abonadas.add(c);
        }
        p.setEstado(calcularEstado(p));
        return abonadas;
    }

    /** calcular el estado del prestamo */
    public EstadoPrestamo calcularEstado(Prestamo p) {
        boolean todasPagadas = true;
        boolean algunaVencida = false;
        for (Cuota c : p.getPlan()) {
            if (c.getEstado() != EstadoCuota.PAGADA) {
                todasPagadas = false;
            }
            if (c.getEstado() == EstadoCuota.VENCIDA) {
                algunaVencida = true;
            }
        }
        if (todasPagadas) {
            return EstadoPrestamo.PAGADO;
        }
        return algunaVencida ? EstadoPrestamo.VENCIDO : EstadoPrestamo.ACTIVO;
    }
}
