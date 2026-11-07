package view;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Lectura de datos por consola. Cada metodo repite la pregunta hasta que el
 * dato sea valido. Se usa un solo Scanner (static) para todo el programa.
 */
public class Validaciones {

    private static Scanner sc = new Scanner(System.in);

    /** Texto que solo tiene letras y espacios (nombres). */
    public String validarTexto(String mensaje) {
        String dato;
        boolean validacion;
        do {
            validacion = true;
            System.out.println(mensaje);
            dato = sc.nextLine();
            if (dato.isBlank()) {
                System.out.println("Error, No se aceptan espacios en blanco");
                validacion = false;
                continue;
            }
            dato = dato.strip();
            for (int i = 0; i < dato.length(); i++) {
                if (Character.isLetter(dato.charAt(i)) == false && dato.charAt(i) != ' ') {
                    System.out.println("Error, solo se aceptan letras o espacios");
                    validacion = false;
                    break;
                }
            }
        } while (validacion == false);
        return dato;
    }

    /** Cualquier texto que no este vacio (direcciones, observaciones obligatorias). */
    public String validarTextoLibre(String mensaje) {
        String dato;
        do {
            System.out.println(mensaje);
            dato = sc.nextLine();
            if (dato.isBlank()) {
                System.out.println("Error, No se aceptan espacios en blanco");
            }
        } while (dato.isBlank());
        return dato.strip();
    }

    /** Texto opcional: si se deja vacio devuelve null. */
    public String validarOpcional(String mensaje) {
        System.out.println(mensaje);
        String dato = sc.nextLine();
        if (dato.isBlank()) {
            return null;
        }
        return dato.strip();
    }

    /** Texto que solo tiene digitos y cuya longitud esta entre minimo y maximo (documento). */
    public String validarNumerico(String mensaje, int minimo, int maximo) {
        String dato;
        boolean validacion;
        do {
            validacion = true;
            System.out.println(mensaje);
            dato = sc.nextLine().strip();
            if (dato.length() < minimo || dato.length() > maximo) {
                System.out.println("Error, debe tener entre " + minimo + " y " + maximo + " digitos");
                validacion = false;
                continue;
            }
            for (int i = 0; i < dato.length(); i++) {
                if (Character.isDigit(dato.charAt(i)) == false) {
                    System.out.println("Error, solo se aceptan numeros");
                    validacion = false;
                    break;
                }
            }
        } while (validacion == false);
        return dato;
    }

    /** Correo con una @ y un punto despues de ella, sin espacios. */
    public String validarCorreo(String mensaje) {
        String dato;
        boolean validacion;
        do {
            System.out.println(mensaje);
            dato = sc.nextLine().strip().toLowerCase();
            int arroba = dato.indexOf('@');
            validacion = arroba > 0 && dato.indexOf('.', arroba) > arroba + 1
                    && !dato.endsWith(".") && !dato.contains(" ") && dato.length() <= 80;
            if (validacion == false) {
                System.out.println("Error, el correo no tiene un formato valido (ejemplo: nombre@correo.com)");
            }
        } while (validacion == false);
        return dato;
    }

    /** Clave de minimo 4 caracteres. */
    public String validarClave(String mensaje) {
        String dato;
        do {
            System.out.println(mensaje);
            dato = sc.nextLine();
            if (dato.isBlank() || dato.length() < 4) {
                System.out.println("Error, la clave debe tener minimo 4 caracteres");
            }
        } while (dato.isBlank() || dato.length() < 4);
        return dato;
    }

    public int validarEntero(String mensaje) {
        System.out.println(mensaje);
        while (sc.hasNextInt() == false) {
            System.out.println("Error, se espera un valor entero");
            sc.next();
        }
        int dato = sc.nextInt();
        sc.nextLine();
        return dato;
    }

    public int validarEntero(String mensaje, int minimo, int maximo) {
        int dato = validarEntero(mensaje);
        while (dato < minimo || dato > maximo) {
            System.out.println("Error, el valor debe estar entre " + minimo + " y " + maximo);
            dato = validarEntero(mensaje);
        }
        return dato;
    }

    /** Valor decimal mayor que 0 (montos y tasas). */
    public double validarDecimal(String mensaje) {
        double dato;
        do {
            System.out.println(mensaje);
            while (sc.hasNextDouble() == false) {
                System.out.println("Error, se espera un valor decimal");
                sc.next();
            }
            dato = sc.nextDouble();
            sc.nextLine();
            if (dato <= 0) {
                System.out.println("Error, el valor debe ser mayor que 0");
            }
        } while (dato <= 0);
        return dato;
    }

    /**
     * Pregunta Si o No en la consola. Devuelve 0 si la respuesta es Si (igual
     * que JOptionPane), asi los controladores comparan con == 0. Se hace en
     * consola porque la ventana de JOptionPane quedaba escondida detras de NetBeans.
     */
    public int confirmar(String texto) {
        int op = validarEntero(texto + " (1- Si  2- No)", 1, 2);
        return op == 1 ? 0 : 1;
    }

    /** Fecha con formato AAAA-MM-DD. */
    public LocalDate validarFecha(String mensaje) {
        do {
            try {
                System.out.println(mensaje);
                return LocalDate.parse(sc.nextLine().strip());
            } catch (Exception e) {
                System.out.println("Error, la fecha debe tener el formato AAAA-MM-DD");
            }
        } while (true);
    }
}
