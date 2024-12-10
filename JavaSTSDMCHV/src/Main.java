import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Создание таблиц в базе данных
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            // таблица автомобилей
            String createCarsTable = "CREATE TABLE IF NOT EXISTS cars " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT, type VARCHAR(255), model VARCHAR(255), brand VARCHAR(255), price DOUBLE, status VARCHAR(20) DEFAULT 'доступен')";
            stmt.execute(createCarsTable);
            // таблица клиентов
            String createCustomersTable = "CREATE TABLE IF NOT EXISTS customers " +
                    "(customer_name VARCHAR(255) PRIMARY KEY, age INT, gender VARCHAR(10))";
            stmt.execute(createCustomersTable);
            // таблица продаж
            String createSalesTable = "CREATE TABLE IF NOT EXISTS sales " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT, car_id INT, customer_name VARCHAR(255), age INT, gender VARCHAR(10), " +
                    "type VARCHAR(255), model VARCHAR(255), brand VARCHAR(255), price DOUBLE, " +
                    "FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (customer_name) REFERENCES customers(customer_name))";
            stmt.execute(createSalesTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            CarShowroomDAO showroomDAO = new CarShowroomDAOImplementation();
            CarShowroomGUI gui = new CarShowroomGUI(showroomDAO);
            gui.setVisible(true);
        });
    }
}
