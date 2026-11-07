package view.cliente.imprimir;

import java.util.List;
import model.entity.Cliente;

public class ImprimirCliente {

    public void entity(Cliente cliente) {
        System.out.println(cliente);
    }

    public void list(List<Cliente> lista) {
        for (Cliente c : lista) {
            entity(c);
        }
    }
}
