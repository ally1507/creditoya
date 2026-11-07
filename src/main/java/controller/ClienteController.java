package controller;

import dao.ClienteDAO;
import dao.IClienteDAO;
import java.util.List;
import model.entity.Cliente;
import view.Mensajes;
import view.cliente.imprimir.ImprimirCliente;
import view.cliente.imprimir.MenuCliente;
import view.cliente.ingreso.IngresoCliente;

/** Gestion de clientes (administrador y asesores). */
public class ClienteController {

    private final MenuCliente menu = new MenuCliente();
    private final ImprimirCliente imprimir = new ImprimirCliente();
    private final IngresoCliente ingreso = new IngresoCliente();
    private final IClienteDAO dao = new ClienteDAO();
    private final Mensajes mensajes = new Mensajes();

    private void agregar() {
        mensajes.informativo("===============Modulo agregar cliente===============");
        String documento = ingreso.pedirDocumento();
        if (dao.buscarPorDocumento(documento) != null) {
            mensajes.error("Ya existe un cliente con ese documento");
            return;
        }
        Cliente c = new Cliente(ingreso.pedirNombre(), documento, ingreso.pedirCorreo(),
                ingreso.pedirTelefono(), ingreso.pedirClave(), ingreso.pedirDireccion());
        if (dao.agregar(c)) {
            mensajes.exito("Cliente agregado correctamente con id " + c.getId());
        } else {
            mensajes.error("No fue posible agregar el cliente (el documento o correo pueden estar repetidos)");
        }
    }

    private void actualizarDatos(Cliente c) {
        boolean val = true;
        switch (menu.opcionesActualizar()) {
            case 1 -> c.setNombre(ingreso.pedirNombre());
            case 2 -> c.setDocumento(ingreso.pedirDocumento());
            case 3 -> c.setCorreo(ingreso.pedirCorreo());
            case 4 -> c.setTelefono(ingreso.pedirTelefono());
            case 5 -> c.setDireccion(ingreso.pedirDireccion());
            case 6 -> c.setClave(ingreso.pedirClave());
            case 7 -> c.setActivo(ingreso.pedirActivo());
            case 8 -> {
                mensajes.informativo("Operacion cancelada!");
                val = false;
            }
            default -> {
                mensajes.error("Opcion fuera de rango!");
                val = false;
            }
        }
        if (val) {
            if (dao.actualizar(c)) {
                mensajes.exito("Cliente actualizado correctamente!");
            } else {
                mensajes.error("No fue posible actualizar el cliente");
            }
        }
    }

    private void actualizar() {
        mensajes.informativo("===============Modulo actualizar cliente===============");
        imprimir.list(dao.listar());
        Cliente c = dao.buscar(ingreso.pedirId());
        if (c == null) {
            mensajes.error("No se encontro el cliente a actualizar");
        } else {
            actualizarDatos(c);
        }
    }

    private void eliminar() {
        mensajes.informativo("===============Modulo eliminar cliente===============");
        imprimir.list(dao.listar());
        Cliente c = dao.buscar(ingreso.pedirId());
        if (c == null) {
            mensajes.error("El cliente a eliminar no existe");
        } else if (ingreso.confirmar("¿Esta seguro de eliminar a " + c.getNombre() + "?") == 0) {
            if (dao.eliminar(c.getId())) {
                mensajes.exito("Cliente eliminado correctamente!");
            } else {
                mensajes.error("No se pudo eliminar: el cliente tiene solicitudes o prestamos.\n"
                        + "Puede desactivarlo desde la opcion Actualizar.");
            }
        } else {
            mensajes.informativo("Eliminacion cancelada!");
        }
    }

    private void buscar() {
        mensajes.informativo("===============Modulo buscar cliente===============");
        Cliente c = dao.buscarPorDocumento(ingreso.pedirDocumento());
        if (c == null) {
            mensajes.error("El cliente no existe");
        } else {
            imprimir.entity(c);
        }
    }

    private void listar() {
        List<Cliente> clientes = dao.listar();
        if (clientes.isEmpty()) {
            mensajes.informativo("No existen clientes a imprimir aun");
        } else {
            imprimir.list(clientes);
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
                case 6 -> mensajes.informativo("Regresando al menu principal...");
                default -> mensajes.error("La opcion esta fuera de rango");
            }
        } while (op != 6);
    }
}
