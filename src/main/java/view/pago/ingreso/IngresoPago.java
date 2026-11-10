package view.pago.ingreso;

import java.time.LocalDate;
import model.entity.MetodoPago;
import view.Validaciones;


public class IngresoPago {

    Validaciones v = new Validaciones();

    public int pedirIdPrestamo() {
        return v.validarEntero("Ingrese el id del prestamo");
    }

    public double pedirMonto() {
        return v.validarDecimal("Ingrese el valor a pagar (sin puntos de miles)");
    }

    public MetodoPago pedirMetodo() {
        int op = v.validarEntero("Metodo de pago: 1- EFECTIVO  2- TRANSFERENCIA  3- TARJETA", 1, 3);
        return MetodoPago.values()[op - 1];
    }

    public String pedirObservaciones() {
        return v.validarOpcional("Observaciones (opcional, Enter para omitir)");
    }

    public LocalDate pedirFecha(String mensaje) {
        return v.validarFecha(mensaje + " (AAAA-MM-DD)");
    }

    /** para confirmar Si/No (0 = Si) */
    public int confirmar(String texto) {
        return v.confirmar(texto);
    }
}
