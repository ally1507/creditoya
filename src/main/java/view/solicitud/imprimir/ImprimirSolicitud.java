package view.solicitud.imprimir;

import java.util.List;
import model.entity.Solicitud;

public class ImprimirSolicitud {

    public void entity(Solicitud solicitud) {
        System.out.println(solicitud);
    }

    public void list(List<Solicitud> lista) {
        for (Solicitud s : lista) {
            entity(s);
        }
    }
}
