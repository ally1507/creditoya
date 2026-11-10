package view.cliente.ingreso;

import view.persona.ingreso.IngresoPersona;

/**  pedir los datos del cliente */
public class IngresoCliente extends IngresoPersona {

    /** Mtdo para pedir la direccion */
    public String pedirDireccion() {
        return v.validarTextoLibre("Ingrese la direccion");
    }
}
