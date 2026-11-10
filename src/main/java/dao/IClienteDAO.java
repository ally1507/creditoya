package dao;

import java.util.List;
import model.entity.Cliente;

/** Intrfz con las operaciones de clientes */
public interface IClienteDAO {

    /** Mtdo para guardar un cliente */
    boolean agregar(Cliente cliente);

    boolean actualizar(Cliente cliente);

    boolean eliminar(int id);

    Cliente buscar(int id);

    Cliente buscarPorDocumento(String documento);

    /** para traer todos los clientes */
    List<Cliente> listar();
}
