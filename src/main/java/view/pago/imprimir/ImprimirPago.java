package view.pago.imprimir;

import java.util.List;
import model.entity.Pago;

public class ImprimirPago {

    public void entity(Pago pago) {
        System.out.println(pago);
    }

    /** Lista los pagos y al final el total recibido. */
    public void list(List<Pago> lista) {
        double total = 0;
        for (Pago p : lista) {
            entity(p);
            total += p.getMonto();
        }
        System.out.println("Pagos: %d    Total: $%,.2f\n".formatted(lista.size(), total));
    }
}
