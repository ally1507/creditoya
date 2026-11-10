package model.logica;

import model.entity.Persona;

/** Singleton del usuario en sesion */
public class UsuarioSesion {

    private static UsuarioSesion instancia;
    private Persona usuario;

    /** Cnstrc privado Singleton */
    private UsuarioSesion() {
    }

    /** devuelve la unica sesion */
    public static UsuarioSesion getInstance() {
        if (instancia == null) {
            instancia = new UsuarioSesion();
        }
        return instancia;
    }

    /**  devuelve el usuario en sesion */
    public Persona getUsuario() {
        return usuario;
    }

    /**  guardar el usuario en sesion */
    public void setUsuario(Persona usuario) {
        this.usuario = usuario;
    }

    /** cerrar la sesion */
    public void cerrar() {
        usuario = null;
    }
}
