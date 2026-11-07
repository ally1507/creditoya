package view.persona.ingreso;

import view.Validaciones;

/**
 * Pide los datos comunes de empleados y clientes. IngresoEmpleado e
 * IngresoCliente heredan de esta clase y agregan sus datos propios.
 */
public class IngresoPersona {

    protected Validaciones v = new Validaciones();

    public String pedirNombre() {
        return v.validarTexto("Ingrese el nombre completo");
    }

    public String pedirDocumento() {
        return v.validarNumerico("Ingrese el numero de documento", 5, 15);
    }

    public String pedirCorreo() {
        return v.validarCorreo("Ingrese el correo");
    }

    /** El telefono es opcional: si se deja vacio queda null. */
    public String pedirTelefono() {
        String telefono = v.validarOpcional("Ingrese el telefono (opcional, Enter para omitir)");
        while (telefono != null && !esTelefonoValido(telefono)) {
            System.out.println("Error, el telefono debe tener entre 7 y 10 digitos");
            telefono = v.validarOpcional("Ingrese el telefono (opcional, Enter para omitir)");
        }
        return telefono;
    }

    private boolean esTelefonoValido(String telefono) {
        if (telefono.length() < 7 || telefono.length() > 10) {
            return false;
        }
        for (int i = 0; i < telefono.length(); i++) {
            if (!Character.isDigit(telefono.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /** Pide la clave dos veces para evitar errores de digitacion. */
    public String pedirClave() {
        String clave = v.validarClave("Ingrese la clave (minimo 4 caracteres)");
        String repetida = v.validarClave("Repita la clave");
        while (!clave.equals(repetida)) {
            System.out.println("Error, las claves no coinciden");
            clave = v.validarClave("Ingrese la clave (minimo 4 caracteres)");
            repetida = v.validarClave("Repita la clave");
        }
        return clave;
    }

    public boolean pedirActivo() {
        return v.validarEntero("Estado: 1- Activo  2- Inactivo", 1, 2) == 1;
    }

    public int pedirId() {
        return v.validarEntero("Ingrese el id");
    }

    public int confirmar(String texto) {
        return v.confirmar(texto);
    }
}
