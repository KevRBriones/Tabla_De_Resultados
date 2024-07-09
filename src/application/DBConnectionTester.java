package application;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionTester {

    public static boolean testConnection() {
        try (Connection conn = Main.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexión exitosa a la base de datos formula1.");
                return true;
            } else {
                System.out.println("No se pudo establecer la conexión a la base de datos formula1.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL al intentar conectar a la base de datos formula1: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error general al intentar conectar a la base de datos formula1: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        if (testConnection()) {
            System.out.println("La prueba de conexión fue exitosa.");
        } else {
            System.out.println("La prueba de conexión falló.");
        }
    }
}