package model.entity;

/** Cliente de CrediYa (rol CLIENTE). Hereda de Persona. */
public class Cliente extends Persona {

    private String direccion;

    public Cliente(String nombre, String documento, String correo, String telefono, String clave,
            String direccion) {
        super(nombre, documento, correo, telefono, Rol.CLIENTE, clave);
        this.direccion = direccion;
    }

    public Cliente(int id, String nombre, String documento, String correo, String telefono,
            String clave, boolean activo, String direccion) {
        super(id, nombre, documento, correo, telefono, Rol.CLIENTE, clave, activo);
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return """
               ********************
               Id:         %s
               Nombre:     %s
               Documento:  %s
               Correo:     %s
               Telefono:   %s
               Direccion:  %s
               Activo:     %s
               """.formatted(getId(), getNombre(), getDocumento(), getCorreo(), getTelefono(),
                direccion, isActivo() ? "Si" : "No");
    }
}
