package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {

    /** abrir la conexion  */
    public Connection conectar() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/crediya_db";
        String user = "crediya";
        String password = "1234";
        return DriverManager.getConnection(url, user, password);
    }
}
