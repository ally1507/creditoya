package dao;

import java.util.List;
import model.entity.Empleado;

/** Intrfz con las operaciones de empleados */
public interface IEmpleadoDAO {

    /** Mtdo para guardar un empleado */
    boolean agregar(Empleado empleado);

    boolean actualizar(Empleado empleado);

    boolean eliminar(int id);

    Empleado buscar(int id);

    Empleado buscarPorDocumento(String documento);

    /** traer todos los empleados */
    List<Empleado> listar();
}
