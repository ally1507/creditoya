package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import model.entity.Empleado;

/**
 * Persistencia de empleados en archivo serializado (empleados.dat), igual que
 * GestionPeliculas en clase. Cada vez que cambia la base de datos se vuelve a
 * escribir la lista completa en el archivo.
 */
public class ArchivoEmpleado {

    private List<Empleado> empleados = new ArrayList<>();
    private final String ARCHIVO = "empleados.dat";

    public ArchivoEmpleado() {
        cargar();
    }

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

    public void guardar(List<Empleado> lista) {
        empleados = lista;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(empleados);
        } catch (Exception e) {
            System.out.println("Error al guardar el archivo de empleados!\n" + e.getMessage());
        }
    }

    public List<Empleado> listar() {
        return empleados;
    }
}
