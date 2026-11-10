package view.persona.ingreso;

import view.Validaciones;

/** pedir los datos comunes de una persona */
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

    /** telefono opcional */
    public String pedirTelefono() {
        String telefono = v.validarOpcional("Ingrese el telefono (opcional, Enter para omitir)");
        while (telefono != null && !esTelefonoValido(telefono)) {
            System.out.println("Error, el telefono debe tener entre 7 y 10 digitos");
            telefono = v.validarOpcional("Ingrese el telefono (opcional, Enter para omitir)");
        }
        return telefono;
    }

    /** validar el telefono */
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

    /** pedir la clave dos veces */
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

    /** pedir si esta activo */
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
