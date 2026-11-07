package view.cliente.ingreso;

import view.persona.ingreso.IngresoPersona;

public class IngresoCliente extends IngresoPersona {

    public String pedirDireccion() {
        return v.validarTextoLibre("Ingrese la direccion");
    }
}
