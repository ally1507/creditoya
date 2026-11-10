package model.entity;

/** hereda de Persona */
public class Cliente extends Persona {

    private String direccion;

    /** Cnstrc para un cliente nuevo (sin id) */
    public Cliente(String nombre, String documento, String correo, String telefono, String clave,
            String direccion) {
        super(nombre, documento, correo, telefono, Rol.CLIENTE, clave);
        this.direccion = direccion;
    }

    /** Cnstrc para un cliente con todos los datos */
    public Cliente(int id, String nombre, String documento, String correo, String telefono,
            String clave, boolean activo, String direccion) {
        super(id, nombre, documento, correo, telefono, Rol.CLIENTE, clave, activo);
        this.direccion = direccion;
    }

    /** devuelve la direccion */
    public String getDireccion() {
        return direccion;
    }

    /** cambiar la direccion */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /** Mtdo para mostrar el cliente */
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
