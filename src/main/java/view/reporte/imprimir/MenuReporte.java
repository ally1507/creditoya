package view.reporte.imprimir;

import view.Validaciones;

public class MenuReporte {

    Validaciones v = new Validaciones();

    public int menu() {
        return v.validarEntero("""
                               Reportes:
                               1- Resumen de cartera por estado.
                               2- Cuotas en mora.
                               3- Recaudo entre fechas por metodo de pago.
                               4- Solicitudes por estado.
                               5- Prestamos activos.
                               6- Prestamos vencidos.
                               7- Clientes morosos.
                               8- Regresar.
                               """);
    }
}
