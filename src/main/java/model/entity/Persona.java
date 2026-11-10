package model.entity;

import java.io.Serializable;

/**datos comunes de empleado y cliente */
public abstract class Persona implements Serializable {

    private int id;
    private String nombre;
    private String documento;
    private String correo;
    private String telefono;
    private Rol rol;
    private String clave;
    private boolean activo;

    /** Cnstrc para una persona nueva (activa, sin id) */
    public Persona(String nombre, String documento, String correo, String telefono, Rol rol, String clave) {
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.telefono = telefono;
        this.rol = rol;
        this.clave = clave;
        this.activo = true;
    }

    /** Cnstrc para una persona con todos los datos */
    public Persona(int id, String nombre, String documento, String correo, String telefono, Rol rol,
            String clave, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.telefono = telefono;
        this.rol = rol;
        this.clave = clave;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    /** para cambiar el id */
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    /**  para cambiar el nombre */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    /**  para cambiar el documento */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /** devuelve el correo */
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**  devuelve el rol */
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public boolean isActivo() {
        return activo;
    }

    /**  activar o desactivar */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
