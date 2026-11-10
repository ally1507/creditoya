package model.entity;

/**estados del prestamo */
public enum EstadoPrestamo {
    PENDIENTE,
    ACTIVO,
    PAGADO,
    /** Con alguna cuota vencida */
    VENCIDO;
}
