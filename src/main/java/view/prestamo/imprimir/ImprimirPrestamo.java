package view.prestamo.imprimir;

import java.util.List;
import model.entity.Cuota;
import model.entity.Prestamo;

/** imprimir prestamos */
public class ImprimirPrestamo {

    public void entity(Prestamo prestamo) {
        System.out.println(prestamo);
    }

    public void list(List<Prestamo> lista) {
        for (Prestamo p : lista) {
            entity(p);
        }
    }

    /** imprimir el prestamo con sus cuotas la tabla */
    public void plan(Prestamo prestamo) {
        entity(prestamo);
        System.out.println("  # | Vence      |        Capital |      Interes |          Cuota |"
                + "        Abonado |  Saldo capital | Estado");
        System.out.println("-".repeat(112));
        for (Cuota c : prestamo.getPlan()) {
            System.out.println(c);
        }
        System.out.println("-".repeat(112));
        System.out.println("Total abonado: $%,.2f    Saldo pendiente: $%,.2f\n".formatted(
                prestamo.getTotalAbonado(), prestamo.getSaldoPendiente()));
    }
}
