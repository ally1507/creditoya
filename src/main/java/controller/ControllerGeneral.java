package controller;

import dao.ClienteDAO;
import dao.EmpleadoDAO;
import dao.IClienteDAO;
import dao.IEmpleadoDAO;
import dao.IPrestamoDAO;
import dao.PrestamoDAO;
import model.entity.Cliente;
import model.entity.Empleado;
import model.entity.Persona;
import model.entity.Rol;
import model.logica.UsuarioSesion;
import view.Mensajes;
import view.MenuPrincipal;
import view.persona.ingreso.IngresoPersona;

/**
 * Inicio de sesion y menu principal segun el rol:
 * ADMIN ve todo, ASESOR todo menos empleados, CLIENTE solo su informacion.
 */
public class ControllerGeneral {

    private final MenuPrincipal menu = new MenuPrincipal();
    private final IngresoPersona ingresoPersona = new IngresoPersona();
    private final IEmpleadoDAO empleadoDao = new EmpleadoDAO();
    private final IClienteDAO clienteDao = new ClienteDAO();
    private final IPrestamoDAO prestamoDao = new PrestamoDAO();
    private final Mensajes mensajes = new Mensajes();
    private int intentosFallidos = 0;

    public void gestionarMenu() {
        int op;
        do {
            op = menu.menuInicio();
            switch (op) {
                case 1 -> {
                    if (iniciarSesion()) {
                        abrirSesion();
                    } else if (intentosFallidos >= 3) {
                        mensajes.error("Demasiados intentos fallidos. El programa se cerrara.");
                        op = 2;
                    }
                }
                case 2 -> mensajes.informativo("Gracias por usar CrediYa!");
                default -> mensajes.error("Opcion no encontrada!");
            }
        } while (op != 2);
    }

    /**
     * Busca al usuario por documento (primero como empleado y luego como
     * cliente) y compara la clave. Si algo esta mal se da el mismo mensaje
     * para no revelar que documentos existen.
     */
    private boolean iniciarSesion() {
        mensajes.informativo("===============Iniciar sesion===============");
        String documento = menu.pedirDocumento();
        String clave = menu.pedirClave();

        Persona usuario = empleadoDao.buscarPorDocumento(documento);
        if (usuario == null) {
            usuario = clienteDao.buscarPorDocumento(documento);
        }
        if (usuario == null || !usuario.getClave().equals(clave)) {
            intentosFallidos++;
            mensajes.error("Documento o clave incorrectos (intento " + intentosFallidos + " de 3)");
            return false;
        }
        if (!usuario.isActivo()) {
            mensajes.error("El usuario esta inactivo. Comuniquese con el administrador");
            return false;
        }
        intentosFallidos = 0;
        UsuarioSesion.getInstance().setUsuario(usuario);
        mensajes.exito("Bienvenido(a) " + usuario.getNombre() + " - " + usuario.getRol());
        return true;
    }

    private void abrirSesion() {
        // Antes de mostrar el menu se marcan las cuotas que ya se vencieron.
        int vencidas = prestamoDao.actualizarVencimientos();
        Persona usuario = UsuarioSesion.getInstance().getUsuario();
        if (vencidas > 0 && usuario.getRol() != Rol.CLIENTE) {
            mensajes.informativo("Se marcaron " + vencidas + " cuotas como vencidas");
        }
        if (usuario.getRol() == Rol.ADMIN) {
            menuAdmin();
        } else if (usuario.getRol() == Rol.ASESOR) {
            menuAsesor();
        } else {
            menuCliente();
        }
        UsuarioSesion.getInstance().cerrar();
        mensajes.informativo("Sesion cerrada");
    }

    private void menuAdmin() {
        int op;
        do {
            op = menu.menuAdmin(UsuarioSesion.getInstance().getUsuario());
            switch (op) {
                case 1 -> new EmpleadoController().menu();
                case 2 -> new ClienteController().menu();
                case 3 -> new SolicitudController().menu();
                case 4 -> new PrestamoController().menu();
                case 5 -> new PagoController().menu();
                case 6 -> new ReporteController().menu();
                case 7 -> cambiarClave();
                case 8 -> {
                }
                default -> mensajes.error("Opcion no encontrada!");
            }
        } while (op != 8);
    }

    private void menuAsesor() {
        int op;
        do {
            op = menu.menuAsesor(UsuarioSesion.getInstance().getUsuario());
            switch (op) {
                case 1 -> new ClienteController().menu();
                case 2 -> new SolicitudController().menu();
                case 3 -> new PrestamoController().menu();
                case 4 -> new PagoController().menu();
                case 5 -> new ReporteController().menu();
                case 6 -> cambiarClave();
                case 7 -> {
                }
                default -> mensajes.error("Opcion no encontrada!");
            }
        } while (op != 7);
    }

    private void menuCliente() {
        PortalClienteController portal = new PortalClienteController();
        int op;
        do {
            op = menu.menuCliente(UsuarioSesion.getInstance().getUsuario());
            switch (op) {
                case 1 -> portal.misPrestamos();
                case 2 -> portal.planDeCuotas();
                case 3 -> portal.misPagos();
                case 4 -> portal.misSolicitudes();
                case 5 -> cambiarClave();
                case 6 -> {
                }
                default -> mensajes.error("Opcion no encontrada!");
            }
        } while (op != 6);
    }

    /** Pide la clave actual antes de dejar poner una nueva. */
    private void cambiarClave() {
        mensajes.informativo("===============Cambiar mi clave===============");
        Persona usuario = UsuarioSesion.getInstance().getUsuario();
        if (!usuario.getClave().equals(menu.pedirClave())) {
            mensajes.error("La clave actual no es correcta");
            return;
        }
        String claveAnterior = usuario.getClave();
        usuario.setClave(ingresoPersona.pedirClave());
        boolean guardado;
        if (usuario.getRol() == Rol.CLIENTE) {
            Cliente c = clienteDao.buscar(usuario.getId());
            c.setClave(usuario.getClave());
            guardado = clienteDao.actualizar(c);
        } else {
            Empleado e = empleadoDao.buscar(usuario.getId());
            e.setClave(usuario.getClave());
            guardado = empleadoDao.actualizar(e);
        }
        if (guardado) {
            mensajes.exito("Clave actualizada correctamente");
        } else {
            usuario.setClave(claveAnterior);
            mensajes.error("No fue posible cambiar la clave");
        }
    }
}
