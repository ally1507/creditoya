package model.entity;

/** Empleado de CrediYa (rol ADMIN o ASESOR). Hereda de Persona. */
public class Empleado extends Persona {

    private double salario;

    public Empleado(String nombre, String documento, String correo, String telefono, Rol rol,
            String clave, double salario) {
        super(nombre, documento, correo, telefono, rol, clave);
        this.salario = salario;
    }

    public Empleado(int id, String nombre, String documento, String correo, String telefono, Rol rol,
            String clave, boolean activo, double salario) {
        super(id, nombre, documento, correo, telefono, rol, clave, activo);
        this.salario = salario;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
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
               Rol:        %s
               Salario:    %,.0f
               Activo:     %s
               """.formatted(getId(), getNombre(), getDocumento(), getCorreo(), getTelefono(),
                getRol(), salario, isActivo() ? "Si" : "No");
    }
}
