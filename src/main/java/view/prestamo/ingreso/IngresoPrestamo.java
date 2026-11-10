package view.prestamo.ingreso;

import model.entity.EstadoPrestamo;
import view.Validaciones;

/** pedir los datos del prestamo */
public class IngresoPrestamo {

    Validaciones v = new Validaciones();

    public int pedirId() {
        return v.validarEntero("Ingrese el id del prestamo");
    }

    public String pedirDocumentoCliente() {
        return v.validarNumerico("Ingrese el documento del cliente", 5, 15);
    }

    public EstadoPrestamo pedirNuevoEstado() {
        int op = v.validarEntero("Nuevo estado: 1- PENDIENTE  2- ACTIVO  3- PAGADO", 1, 3);
        if (op == 1) {
            return EstadoPrestamo.PENDIENTE;
        } else if (op == 2) {
            return EstadoPrestamo.ACTIVO;
        }
        return EstadoPrestamo.PAGADO;
    }

    public EstadoPrestamo pedirEstado() {
        int op = v.validarEntero("Estado: 1- ACTIVO  2- VENCIDO  3- PAGADO", 1, 3);
        if (op == 1) {
            return EstadoPrestamo.ACTIVO;
        } else if (op == 2) {
            return EstadoPrestamo.VENCIDO;
        }
        return EstadoPrestamo.PAGADO;
    }

    public int confirmar(String texto) {
        return v.confirmar(texto);
    }
}
