package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import model.entity.Empleado;

/** Cls para guardar los empleados en empleados.dat */
public class ArchivoEmpleado {

    private List<Empleado> empleados = new ArrayList<>();
    private final String ARCHIVO = "empleados.dat";

    /** Cnstrc que carga empleados.dat */
    public ArchivoEmpleado() {
        cargar();
    }

    /** Mtdo para leer empleados.dat */
    public void cargar() {
        File f = new File(ARCHIVO);
        if (!f.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            empleados = (List<Empleado>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo de empleados!\n" + e.getMessage());
        }
    }

    /** Mtdo para escribir los empleados en empleados.dat */
    public void guardar(List<Empleado> lista) {
        empleados = lista;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(empleados);
        } catch (Exception e) {
            System.out.println("Error al guardar el archivo de empleados!\n" + e.getMessage());
        }
    }

    /** Mtdo para traer los empleados del archivo */
    public List<Empleado> listar() {
        return empleados;
    }
}
