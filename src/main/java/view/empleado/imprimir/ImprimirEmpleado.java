package view.empleado.imprimir;

import java.util.List;
import model.entity.Empleado;

public class ImprimirEmpleado {

    public void entity(Empleado empleado) {
        System.out.println(empleado);
    }

    public void list(List<Empleado> lista) {
        for (Empleado e : lista) {
            entity(e);
        }
    }
}
