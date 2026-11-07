package view.empleado.ingreso;

import model.entity.Rol;
import view.persona.ingreso.IngresoPersona;

public class IngresoEmpleado extends IngresoPersona {

    public Rol pedirRol() {
        int op = v.validarEntero("Seleccione el rol: 1- ADMIN  2- ASESOR", 1, 2);
        return op == 1 ? Rol.ADMIN : Rol.ASESOR;
    }

    public double pedirSalario() {
        return v.validarDecimal("Ingrese el salario (sin puntos de miles)");
    }
}
