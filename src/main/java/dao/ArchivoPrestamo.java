package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import model.entity.Prestamo;

/**
 * Persistencia de prestamos (con su plan de cuotas) en archivo serializado
 * (prestamos.dat), igual que GestionPeliculas en clase. Cada vez que se crea
 * un prestamo, se paga o cambia de estado se reescribe el archivo.
 */
public class ArchivoPrestamo {

    private List<Prestamo> prestamos = new ArrayList<>();
    private final String ARCHIVO = "prestamos.dat";

    public ArchivoPrestamo() {
        cargar();
    }

    public void cargar() {
        File f = new File(ARCHIVO);
        if (!f.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            prestamos = (List<Prestamo>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo de prestamos!\n" + e.getMessage());
        }
    }

    public void guardar(List<Prestamo> lista) {
        prestamos = lista;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(prestamos);
        } catch (Exception e) {
            System.out.println("Error al guardar el archivo de prestamos!\n" + e.getMessage());
        }
    }

    public List<Prestamo> listar() {
        return prestamos;
    }
}
