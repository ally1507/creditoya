package dao;

import java.util.List;
import model.entity.Empleado;

/**
 * Contrato de acceso a datos de los empleados. El controlador solo conoce
 * estos metodos y no sabe como se guardan los datos (EmpleadoDAO usa MySQL).
 */
public interface IEmpleadoDAO {

    boolean agregar(Empleado empleado);

    boolean actualizar(Empleado empleado);

    boolean eliminar(int id);

    Empleado buscar(int id);

    Empleado buscarPorDocumento(String documento);

    List<Empleado> listar();
}
