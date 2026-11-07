package controller;

import dao.ArchivoEmpleado;
import dao.EmpleadoDAO;
import dao.IEmpleadoDAO;
import java.util.List;
import model.entity.Empleado;
import model.logica.UsuarioSesion;
import view.Mensajes;
import view.empleado.imprimir.ImprimirEmpleado;
import view.empleado.imprimir.MenuEmpleado;
import view.empleado.ingreso.IngresoEmpleado;

/** Gestion de empleados. Solo el administrador entra a este modulo. */
public class EmpleadoController {

    private final MenuEmpleado menu = new MenuEmpleado();
    private final ImprimirEmpleado imprimir = new ImprimirEmpleado();
    private final IngresoEmpleado ingreso = new IngresoEmpleado();
    private final IEmpleadoDAO dao = new EmpleadoDAO();
    private final ArchivoEmpleado archivo = new ArchivoEmpleado();
    private final Mensajes mensajes = new Mensajes();

    /** true si el empleado es el mismo que inicio sesion. */
    private boolean esUsuarioActual(Empleado e) {
        return e.getId() == UsuarioSesion.getInstance().getUsuario().getId();
    }

    private void agregar() {
        mensajes.informativo("===============Modulo agregar empleado===============");
        String documento = ingreso.pedirDocumento();
        if (dao.buscarPorDocumento(documento) != null) {
            mensajes.error("Ya existe un empleado con ese documento");
            return;
        }
        Empleado e = new Empleado(ingreso.pedirNombre(), documento, ingreso.pedirCorreo(),
                ingreso.pedirTelefono(), ingreso.pedirRol(), ingreso.pedirClave(), ingreso.pedirSalario());
        if (dao.agregar(e)) {
            archivo.guardar(dao.listar());
            mensajes.exito("Empleado agregado correctamente con id " + e.getId());
        } else {
            mensajes.error("No fue posible agregar el empleado (el documento o correo pueden estar repetidos)");
        }
    }

    private void actualizarDatos(Empleado e) {
        boolean val = true;
        switch (menu.opcionesActualizar()) {
            case 1 -> e.setNombre(ingreso.pedirNombre());
            case 2 -> e.setDocumento(ingreso.pedirDocumento());
            case 3 -> e.setCorreo(ingreso.pedirCorreo());
            case 4 -> e.setTelefono(ingreso.pedirTelefono());
            case 5 -> {
                if (esUsuarioActual(e)) {
                    mensajes.error("No puede cambiar su propio rol");
                    val = false;
                } else {
                    e.setRol(ingreso.pedirRol());
                }
            }
            case 6 -> e.setSalario(ingreso.pedirSalario());
            case 7 -> e.setClave(ingreso.pedirClave());
            case 8 -> {
                if (esUsuarioActual(e)) {
                    mensajes.error("No puede desactivar su propia cuenta");
                    val = false;
                } else {
                    e.setActivo(ingreso.pedirActivo());
                }
            }
            case 9 -> {
                mensajes.informativo("Operacion cancelada!");
                val = false;
            }
            default -> {
                mensajes.error("Opcion fuera de rango!");
                val = false;
            }
        }
        if (val) {
            if (dao.actualizar(e)) {
                archivo.guardar(dao.listar());
                mensajes.exito("Empleado actualizado correctamente!");
            } else {
                mensajes.error("No fue posible actualizar el empleado");
            }
        }
    }

    private void actualizar() {
        mensajes.informativo("===============Modulo actualizar empleado===============");
        imprimir.list(dao.listar());
        Empleado e = dao.buscar(ingreso.pedirId());
        if (e == null) {
            mensajes.error("No se encontro el empleado a actualizar");
        } else {
            actualizarDatos(e);
        }
    }

    private void eliminar() {
        mensajes.informativo("===============Modulo eliminar empleado===============");
        imprimir.list(dao.listar());
        Empleado e = dao.buscar(ingreso.pedirId());
        if (e == null) {
            mensajes.error("El empleado a eliminar no existe");
        } else if (esUsuarioActual(e)) {
            mensajes.error("No puede eliminar su propia cuenta");
        } else if (ingreso.confirmar("¿Esta seguro de eliminar a " + e.getNombre() + "?") == 0) {
            if (dao.eliminar(e.getId())) {
                archivo.guardar(dao.listar());
                mensajes.exito("Empleado eliminado correctamente!");
            } else {
                mensajes.error("No se pudo eliminar: el empleado tiene solicitudes, prestamos o pagos.\n"
                        + "Puede desactivarlo desde la opcion Actualizar.");
            }
        } else {
            mensajes.informativo("Eliminacion cancelada!");
        }
    }

    private void buscar() {
        mensajes.informativo("===============Modulo buscar empleado===============");
        Empleado e = dao.buscarPorDocumento(ingreso.pedirDocumento());
        if (e == null) {
            mensajes.error("El empleado no existe");
        } else {
            imprimir.entity(e);
        }
    }

    private void listar() {
        List<Empleado> empleados = dao.listar();
        if (empleados.isEmpty()) {
            mensajes.informativo("No existen empleados a imprimir aun");
        } else {
            imprimir.list(empleados);
        }
    }

    /** Muestra lo que esta guardado en empleados.dat (no consulta MySQL). */
    private void listarArchivo() {
        mensajes.informativo("===============Empleados guardados en archivo===============");
        List<Empleado> empleados = archivo.listar();
        if (empleados.isEmpty()) {
            mensajes.informativo("El archivo de empleados esta vacio");
        } else {
            imprimir.list(empleados);
        }
    }

    public void menu() {
        int op;
        do {
            op = menu.menu();
            switch (op) {
                case 1 -> agregar();
                case 2 -> actualizar();
                case 3 -> eliminar();
                case 4 -> buscar();
                case 5 -> listar();
                case 6 -> listarArchivo();
                case 7 -> mensajes.informativo("Regresando al menu principal...");
                default -> mensajes.error("La opcion esta fuera de rango");
            }
        } while (op != 7);
    }
}
