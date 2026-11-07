package dao;

import java.util.List;
import model.entity.Cliente;

/** Contrato de acceso a datos de los clientes. */
public interface IClienteDAO {

    boolean agregar(Cliente cliente);

    boolean actualizar(Cliente cliente);

    boolean eliminar(int id);

    Cliente buscar(int id);

    Cliente buscarPorDocumento(String documento);

    List<Cliente> listar();
}
