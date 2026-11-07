package view;

import model.entity.Persona;

/** Menus principales: uno por cada rol, mas la pantalla de inicio de sesion. */
public class MenuPrincipal {

    Validaciones v = new Validaciones();

    public int menuInicio() {
        return v.validarEntero("""

                               ===================================
                                       CrediYa S.A.S.
                               ===================================
                               1- Iniciar sesion.
                               2- Salir.
                               """);
    }

    public String pedirDocumento() {
        return v.validarTextoLibre("Documento:");
    }

    public String pedirClave() {
        return v.validarClave("Clave:");
    }

    public int menuAdmin(Persona usuario) {
        return v.validarEntero("""
                               ===== Administrador: %s =====
                               1- Empleados.
                               2- Clientes.
                               3- Solicitudes.
                               4- Prestamos.
                               5- Pagos.
                               6- Reportes.
                               7- Cambiar mi clave.
                               8- Cerrar sesion.
                               """.formatted(usuario.getNombre()));
    }

    public int menuAsesor(Persona usuario) {
        return v.validarEntero("""
                               ===== Asesor: %s =====
                               1- Clientes.
                               2- Solicitudes.
                               3- Prestamos.
                               4- Pagos.
                               5- Reportes.
                               6- Cambiar mi clave.
                               7- Cerrar sesion.
                               """.formatted(usuario.getNombre()));
    }

    public int menuCliente(Persona usuario) {
        return v.validarEntero("""
                               ===== Cliente: %s =====
                               1- Mis prestamos.
                               2- Plan de cuotas de un prestamo.
                               3- Mis pagos.
                               4- Mis solicitudes.
                               5- Cambiar mi clave.
                               6- Cerrar sesion.
                               """.formatted(usuario.getNombre()));
    }
}
