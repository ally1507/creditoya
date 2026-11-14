package examen;

/** Excepcion personalizada: el prestamo no existe en la base de datos */
public class PrestamoNoEncontradoException extends Exception {

    private final int prestamoId;

    /** Cnstrc con el id que no se encontro */
    public PrestamoNoEncontradoException(int prestamoId) {
        super("No existe un prestamo con id " + prestamoId);
        this.prestamoId = prestamoId;
    }

    /** devuelve el id que no se encontro */
    public int getPrestamoId() {
        return prestamoId;
    }
}