package model.logica;

import model.entity.Persona;

/**
 * Usuario que inicio sesion (patron Singleton, igual que en clase): solo
 * existe una sesion en todo el programa y cualquier controlador la consulta
 * con UsuarioSesion.getInstance().
 */
public class UsuarioSesion {

    private static UsuarioSesion instancia;
    private Persona usuario;

    private UsuarioSesion() {
    }

    public static UsuarioSesion getInstance() {
        if (instancia == null) {
            instancia = new UsuarioSesion();
        }
        return instancia;
    }

    public Persona getUsuario() {
        return usuario;
    }

    public void setUsuario(Persona usuario) {
        this.usuario = usuario;
    }

    public void cerrar() {
        usuario = null;
    }
}
