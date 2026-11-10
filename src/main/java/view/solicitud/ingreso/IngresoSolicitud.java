package view.solicitud.ingreso;

import view.Validaciones;

/**  pedir los datos de la solicitud */
public class IngresoSolicitud {

    Validaciones v = new Validaciones();

    public String pedirDocumentoCliente() {
        return v.validarNumerico("Ingrese el documento del cliente", 5, 15);
    }

    public double pedirMonto() {
        return v.validarDecimal("Ingrese el monto solicitado (sin puntos de miles)");
    }

    public int pedirCuotas() {
        return v.validarEntero("Ingrese el numero de cuotas mensuales (1 a 120)", 1, 120);
    }

    /**  pedir la tasa E.A. */
    public double pedirTasaEa() {
        double tasa = v.validarDecimal("Ingrese la tasa efectiva anual en % (ejemplo: 24)");
        while (tasa > 100) {
            System.out.println("Error, la tasa no puede ser mayor a 100%");
            tasa = v.validarDecimal("Ingrese la tasa efectiva anual en % (ejemplo: 24)");
        }
        return tasa;
    }

    /**  observaciones (opcional) */
    public String pedirObservaciones() {
        return v.validarOpcional("Observaciones (opcional, Enter para omitir)");
    }

    public String pedirMotivoRechazo() {
        return v.validarTextoLibre("Ingrese el motivo del rechazo");
    }

    public int pedirId() {
        return v.validarEntero("Ingrese el id de la solicitud");
    }

    public int confirmar(String texto) {
        return v.confirmar(texto);
    }
}
