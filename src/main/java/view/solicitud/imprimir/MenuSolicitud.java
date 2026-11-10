package view.solicitud.imprimir;

import view.Validaciones;

public class MenuSolicitud {

    Validaciones v = new Validaciones();

    public int menu() {
        return v.validarEntero("""
                               Opciones de Solicitudes:
                               1- Registrar solicitud.
                               2- Ver solicitudes pendientes.
                               3- Aprobar solicitud.
                               4- Rechazar solicitud.
                               5- Buscar solicitud.
                               6- Listar todas.
                               7- Regresar.
                               """);
    }
}
