package view.cliente.imprimir;

import view.Validaciones;

public class MenuCliente {

    Validaciones v = new Validaciones();

    /** mostrar el menu de clientes */
    public int menu() {
        return v.validarEntero("""
                               Opciones de Clientes:
                               1- Agregar.
                               2- Actualizar.
                               3- Eliminar.
                               4- Buscar por documento.
                               5- Listar.
                               6- Regresar.
                               """);
    }

    /** escoger que dato actualizar */
    public int opcionesActualizar() {
        return v.validarEntero("""
                               Ingrese que desea actualizar:
                               1- Nombre.
                               2- Documento.
                               3- Correo.
                               4- Telefono.
                               5- Direccion.
                               6- Clave.
                               7- Activar / desactivar.
                               8- Cancelar.
                               """);
    }
}
