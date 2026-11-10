package view.empleado.imprimir;

import view.Validaciones;

public class MenuEmpleado {

    Validaciones v = new Validaciones();

    public int menu() {
        return v.validarEntero("""
                               Opciones de Empleados:
                               1- Agregar.
                               2- Actualizar.
                               3- Eliminar.
                               4- Buscar por documento.
                               5- Listar.
                               6- Ver empleados guardados en archivo.
                               7- Regresar.
                               """);
    }

    public int opcionesActualizar() {
        return v.validarEntero("""
                               Ingrese que desea actualizar:
                               1- Nombre.
                               2- Documento.
                               3- Correo.
                               4- Telefono.
                               5- Rol.
                               6- Salario.
                               7- Clave.
                               8- Activar / desactivar.
                               9- Cancelar.
                               """);
    }
}
