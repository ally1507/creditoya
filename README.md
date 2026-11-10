# CrediYa S.A.S.

Sistema de consola desarrollado en Java para manejar los empleados, clientes, solicitudes de credito, prestamos, cuotas y pagos de CrediYa. La aplicacion usa MySQL mediante JDBC, sigue la arquitectura MVC con DAO y guarda copias de respaldo en archivos serializados.

## Contenido

- [Caracteristicas](#caracteristicas)
- [Tecnologias](#tecnologias)
- [Arquitectura](#arquitectura)
- [Modelo de negocio](#modelo-de-negocio)
- [Manual de uso en NetBeans](#manual-de-uso-en-netbeans)
- [Patrones y conceptos aplicados](#patrones-y-conceptos-aplicados)
- [Estructura del proyecto](#estructura-del-proyecto)

---

## Caracteristicas

### Gestion de usuarios
- Inicio de sesion con documento y clave (maximo 3 intentos).
- Menus diferentes segun el rol: **administrador**, **asesor** o **cliente**.
- Registro, actualizacion, busqueda, listado y eliminacion de empleados (solo el administrador).
- Registro, actualizacion, busqueda, listado y eliminacion de clientes.
- Activar o desactivar usuarios; un usuario inactivo no puede entrar.
- Cada usuario puede cambiar su propia clave.

### Solicitudes y prestamos
- Registro de solicitudes de credito con monto y numero de cuotas (1 a 120).
- Aprobacion o rechazo de solicitudes pendientes.
- Al aprobar se pide la tasa efectiva anual y se muestra una vista previa del plan de cuotas antes de confirmar.
- Calculo de la cuota fija con el **sistema frances**.
- Consulta del prestamo con su plan de cuotas, por cliente, por estado o todos.
- Estados del prestamo: PENDIENTE, ACTIVO, VENCIDO y PAGADO.

### Pagos
- Registro de abonos parciales o totales.
- Metodos de pago: efectivo, transferencia o tarjeta.
- El pago se reparte automaticamente desde la cuota mas antigua.
- El prestamo pasa a PAGADO cuando se cubre toda la deuda.
- Consulta de pagos por prestamo y entre fechas.
- Al iniciar sesion, las cuotas atrasadas se marcan como VENCIDAS.

### Reportes y respaldos
- Resumen de cartera por estado.
- Cuotas en mora con dias de atraso.
- Recaudo entre fechas por metodo de pago.
- Solicitudes por estado.
- Prestamos activos, prestamos vencidos y clientes morosos (con Lambda y Stream API).
- Respaldo automatico de empleados y prestamos en `empleados.dat` y `prestamos.dat`.

### Portal del cliente
- El cliente puede ver sus prestamos, el plan de cuotas, sus pagos y sus solicitudes.

---

## Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 | Logica del programa y aplicacion de consola |
| Maven | Dependencias y compilacion |
| MySQL 8 | Base de datos |
| JDBC (mysql-connector-j 9.7.0) | Conexion de Java con MySQL |
| NetBeans | IDE de desarrollo |
| Git y GitHub | Control de versiones |

---

## Arquitectura

El proyecto usa MVC y DAO para separar responsabilidades:

```
Vista (view)
    │ muestra menus y pide datos por consola
    ▼
Controlador (controller)
    │ aplica las reglas y coordina las acciones
    ▼
DAO (dao)
    │ ejecuta las consultas SQL con JDBC
    ▼
MySQL (crediya_db)
```

El modelo representa las entidades del negocio:

```
Persona (abstracta)
├── Empleado
└── Cliente

Solicitud
└── Prestamo
    ├── Cuota
    └── Pago
```

---

## Modelo de negocio

### Herencia de personas
`Persona` es una clase abstracta con los datos comunes: id, nombre, documento, correo, telefono, rol, clave y estado activo.

- `Empleado` hereda de `Persona` y agrega el salario.
- `Cliente` hereda de `Persona` y agrega la direccion.

En MySQL pasa lo mismo: la tabla `personas` tiene los datos comunes y las tablas `empleados` y `clientes` tienen los datos de cada uno.

### Sistema frances (cuota fija)

```
Tasa mensual = (1 + tasa EA)^(1/12) - 1
Cuota        = Monto x i / (1 - (1 + i)^-n)
Interes      = Saldo x i
Capital      = Cuota - Interes
```

Donde `i` es la tasa mensual y `n` el numero de cuotas. En MySQL los valores de dinero se guardan como DECIMAL.

---


---

## Manual de uso en NetBeans

> Al ejecutar, el programa aparece en la ventana **Output** de NetBeans. Ahi se escribe el numero de la opcion y se presiona Enter.

### 1. Ejecutar el proyecto
Abrir el proyecto y darle Run (F6). Abajo se abre la ventana Output con el menu de inicio.

 <img width="957" height="349" alt="image" src="https://github.com/user-attachments/assets/36cf79fb-405b-438f-80db-079c7dbba3b4" />



### 2. Iniciar sesion
Elegir `1- Iniciar sesion` y escribir documento y clave.
El primer usuario es el administrador: documento `1000000001`, clave `admin123`.

<img width="435" height="288" alt="image" src="https://github.com/user-attachments/assets/a1921c76-b6e5-47c9-9756-d5abff21527d" />



### 3. Menu del administrador
Muestra todos los modulos: empleados, clientes, solicitudes, prestamos, pagos y reportes.

<img width="476" height="483" alt="image" src="https://github.com/user-attachments/assets/e3feb5cc-8d2d-4349-b888-e0a14f729eed" />



### 4. Registrar un empleado
Empleados > `1- Agregar`. Se pide documento, nombre, correo, telefono, rol, clave y salario.

<img width="550" height="546" alt="image" src="https://github.com/user-attachments/assets/d77c59fb-e68f-4557-af54-5cb90a333e79" />


### 5. Registrar un cliente
Clientes > `1- Agregar`. Se pide documento, nombre, correo, telefono, clave y direccion.

<img width="848" height="697" alt="image" src="https://github.com/user-attachments/assets/07d2846f-f8ab-4d0a-baa6-1fb8ec750e6f" />



### 6. Registrar una solicitud
Solicitudes > `1- Registrar solicitud`. Se escribe el documento del cliente, el monto y el numero de cuotas.

<img width="450" height="683" alt="image" src="https://github.com/user-attachments/assets/7195a809-4e24-4e72-8ecd-2fdf6299c0b8" />



### 7. Aprobar la solicitud
Solicitudes > `3- Aprobar solicitud`. Se escribe el id, la tasa efectiva anual y se ve el plan de cuotas antes de confirmar.

<img width="953" height="796" alt="image" src="https://github.com/user-attachments/assets/2b77bd8f-fa56-41f7-a958-cfaa34d92e26" />



### 8. Registrar un pago
Pagos > `1- Registrar pago`. Se escribe el id del prestamo, el valor y el metodo de pago.

<img width="948" height="823" alt="image" src="https://github.com/user-attachments/assets/d8a7f285-daca-402a-aa4e-ba3e86867ffe" />



### 9. Reportes
Reportes > escoger una opcion (cartera, mora, recaudo, morosos...).

<img width="692" height="781" alt="image" src="https://github.com/user-attachments/assets/e0034938-34e4-4c07-811e-3f03084687d7" />



### 10. Menu del cliente
Al entrar con un cliente solo aparecen sus consultas: prestamos, plan de cuotas, pagos y solicitudes.

<img width="439" height="449" alt="image" src="https://github.com/user-attachments/assets/40c967fb-3cdb-4b40-9a10-37fb1cd7d4e8" />



### 11. Base de datos
Tablas de `crediya_db` vistas desde MySQL Workbench.

<img width="1032" height="850" alt="image" src="https://github.com/user-attachments/assets/5e47e3aa-4b6c-4b3c-9b4c-e9e6b064ce1d" />



### Recomendaciones
- Los decimales se escriben con coma: `24,5`.
- Los montos van sin puntos de miles: `1000000`.
- Las fechas se escriben `AAAA-MM-DD`, por ejemplo `2026-11-09`.
- Para confirmar se escribe `1` (Si) o `2` (No).
- Un cliente o empleado con prestamos no se puede borrar; se debe desactivar.

El manual mas detallado esta en [MANUAL_DE_USO.md](MANUAL_DE_USO.md).

---

## Patrones y conceptos aplicados

| Concepto o patron | Implementacion |
|---|---|
| POO | Clases Persona, Empleado, Cliente, Solicitud, Prestamo, Cuota y Pago |
| Herencia | Empleado y Cliente heredan de Persona |
| Clase abstracta | Persona no se puede instanciar |
| Polimorfismo | `toString()` propio en cada entidad; el login guarda Empleado o Cliente en una variable Persona |
| Encapsulamiento | Atributos privados con getters y setters |
| Interfaces | IClienteDAO, IEmpleadoDAO, ISolicitudDAO, IPrestamoDAO, IPagoDAO |
| Enums | Rol, EstadoSolicitud, EstadoPrestamo, EstadoCuota, MetodoPago |
| Singleton | UsuarioSesion guarda el usuario que inicio sesion |
| MVC | Separacion en model, view y controller |
| DAO | Las consultas SQL estan separadas de la logica |
| Serializable | Respaldos en empleados.dat y prestamos.dat |
| Lambda y Stream API | Filtros de prestamos activos, vencidos y clientes morosos |
| Manejo de excepciones | SQLException e IllegalArgumentException |

---

## Estructura del proyecto

```
creditoya/
├── src/main/java/
│   ├── com/k4/creditoya/   # Clase principal (main)
│   ├── controller/         # Controladores de cada modulo
│   ├── dao/                # Acceso a MySQL y archivos .dat
│   ├── database/           # Clase Conexion
│   ├── model/
│   │   ├── entity/         # Entidades y enums
│   │   └── logica/         # Calculos del credito y sesion
│   └── view/               # Menus, ingreso, impresion y validaciones
├── docs/
│   └── images/             # Capturas del sistema
├── crediya_db.sql          # Script de la base de datos
├── MANUAL_DE_USO.md
├── pom.xml
├── .gitignore
└── README.md
```

---

## Autores

- Allyson Martinez


## 8. Salir

Use **Cerrar sesión** en su menú y luego **2- Salir** en la pantalla de inicio.
