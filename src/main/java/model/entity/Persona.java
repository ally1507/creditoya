package model.entity;

import java.io.Serializable;

/**
 * Datos comunes de empleados y clientes. Es abstracta: no se crea una Persona
 * sola, sino un Empleado o un Cliente (herencia).
 */
public abstract class Persona implements Serializable {

    private int id;
    private String nombre;
    private String documento;
    private String correo;
    private String telefono;
    private Rol rol;
    private String clave;
    private boolean activo;

    public Persona(String nombre, String documento, String correo, String telefono, Rol rol, String clave) {
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.telefono = telefono;
        this.rol = rol;
        this.clave = clave;
        this.activo = true;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

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

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
