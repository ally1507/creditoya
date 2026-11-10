package view.prestamo.imprimir;

import view.Validaciones;

public class MenuPrestamo {

    Validaciones v = new Validaciones();

    public int menu() {
        return v.validarEntero("""
                               Opciones de Prestamos:
                               1- Ver prestamo con plan de cuotas.
                               2- Prestamos de un cliente.
                               3- Prestamos por estado.
                               4- Listar todos.
                               5- Cambiar estado.
                               6- Ver prestamos guardados en archivo.
                               7- Regresar.
                               """);
    }
}
