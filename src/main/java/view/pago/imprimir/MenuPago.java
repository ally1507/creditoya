package view.pago.imprimir;

import view.Validaciones;

public class MenuPago {

    Validaciones v = new Validaciones();

    /**el menu de pagos */
    public int menu() {
        return v.validarEntero("""
                               Opciones de Pagos:
                               1- Registrar pago.
                               2- Pagos de un prestamo.
                               3- Pagos entre fechas.
                               4- Regresar.
                               """);
    }
}
