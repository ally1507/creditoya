package view.reporte.imprimir;

import java.util.List;
import java.util.Map;
import model.entity.CuotaEnMora;
import model.entity.ResumenCartera;

public class ImprimirReporte {

    public void cartera(List<ResumenCartera> filas) {
        System.out.println("Estado     | Cantidad |   Monto prestado   |  Saldo por cobrar");
        System.out.println("-".repeat(66));
        int cantidad = 0;
        double monto = 0;
        double saldo = 0;
        for (ResumenCartera r : filas) {
            System.out.println(r);
            cantidad += r.getCantidad();
            monto += r.getMontoPrestado();
            saldo += r.getSaldoPendiente();
        }
        System.out.println("-".repeat(66));
        System.out.println("%-10s | %8d | %,18.2f | %,18.2f\n".formatted("TOTAL", cantidad, monto, saldo));
    }

    public void mora(List<CuotaEnMora> filas) {
        System.out.println("Prestamo | Cliente                   | Documento    | Telefono   | Cuota | Vencio     |      Pendiente |  Dias");
        System.out.println("-".repeat(112));
        double total = 0;
        for (CuotaEnMora c : filas) {
            System.out.println(c);
            total += c.getPendiente();
        }
        System.out.println("-".repeat(112));
        long prestamos = filas.stream().map(c -> c.getPrestamoId()).distinct().count();
        System.out.println("Cuotas vencidas: %d    Prestamos en mora: %d    Total vencido: $%,.2f\n"
                .formatted(filas.size(), prestamos, total));
    }

    /** Recaudo por metodo de pago (HashMap: metodo -> total). */
    public void recaudo(String titulo, Map<String, Double> totales) {
        System.out.println(titulo);
        double suma = 0;
        for (Map.Entry<String, Double> entry : totales.entrySet()) {
            System.out.println("  %-15s $%,18.2f".formatted(entry.getKey(), entry.getValue()));
            suma += entry.getValue();
        }
        System.out.println("  %-15s $%,18.2f\n".formatted("TOTAL", suma));
    }

    /** Cantidad por estado (HashMap: estado -> cantidad). */
    public void conteo(String titulo, Map<String, Long> conteos) {
        System.out.println(titulo);
        for (Map.Entry<String, Long> entry : conteos.entrySet()) {
            System.out.println("  %-15s %8d".formatted(entry.getKey(), entry.getValue()));
        }
        System.out.println();
    }
}
