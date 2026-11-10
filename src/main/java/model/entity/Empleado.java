package model.entity;


public class Empleado extends Persona {

    private double salario;

    /** Cnstrc para un empleado nuevo (sin id) */
    public Empleado(String nombre, String documento, String correo, String telefono, Rol rol,
            String clave, double salario) {
        super(nombre, documento, correo, telefono, rol, clave);
        this.salario = salario;
    }

    /** Cnstrc para un empleado con todos los datos */
    public Empleado(int id, String nombre, String documento, String correo, String telefono, Rol rol,
            String clave, boolean activo, double salario) {
        super(id, nombre, documento, correo, telefono, rol, clave, activo);
        this.salario = salario;
    }

    /** d el salario */
    public double getSalario() {
        return salario;
    }

    /** cambiar el salario */
    public void setSalario(double salario) {
        this.salario = salario;
    }

    /**  mostrar el empleado */
    @Override
    public String toString() {
        return """
               ********************
               Id:         %s
               Nombre:     %s
               Documento:  %s
               Correo:     %s
               Telefono:   %s
               Rol:        %s
               Salario:    %,.0f
               Activo:     %s
               """.formatted(getId(), getNombre(), getDocumento(), getCorreo(), getTelefono(),
                getRol(), salario, isActivo() ? "Si" : "No");
    }
}
