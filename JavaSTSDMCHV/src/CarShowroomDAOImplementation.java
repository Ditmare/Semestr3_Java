import java.sql.*;
import java.util.*;

public class CarShowroomDAOImplementation implements CarShowroomDAO {
    @Override
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Car car = new Car(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("model"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getString("status")
                );
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getString("customer_name"),
                        rs.getInt("age"),
                        rs.getString("gender")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public void sellCar(Car car, Customer customer) {
        if ("продан".equals(car.getStatus())) {
            throw new IllegalStateException("Автомобиль уже продан. Невозможно выполнить продажу.");
        }

        String query = "INSERT INTO sales (car_id, customer_name, age, gender) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, car.getId());
            pstmt.setString(2, customer.getName());
            pstmt.setInt(3, customer.getAge());
            pstmt.setString(4, customer.getGender());
            pstmt.executeUpdate();
            updateCarStatus(car.getId(), "продан");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updateCarStatus(int carId, String status) {
        String query = "UPDATE cars SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, carId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map.Entry<Car, Customer>> getSales() {
        List<Map.Entry<Car, Customer>> sales = new ArrayList<>();
        String query = "SELECT c.*, s.customer_name, s.age, s.gender FROM sales s JOIN cars c ON s.car_id = c.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Car car = new Car(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("model"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        "продан"
                );
                Customer customer = new Customer(rs.getString("customer_name"), rs.getInt("age"), rs.getString("gender"));
                sales.add(new AbstractMap.SimpleEntry<>(car, customer));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }

    @Override
    public void addCar(Car car) {
        String query = "INSERT INTO cars (type, model, brand, price, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, car.getType());
            pstmt.setString(2, car.getModel());
            pstmt.setString(3, car.getBrand());
            pstmt.setDouble(4, car.getPrice());
            pstmt.setString(5, car.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCar(Car car) {
        String query = "UPDATE cars SET type = ?, model = ?, brand = ?, price = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, car.getType());
            pstmt.setString(2, car.getModel());
            pstmt.setString(3, car.getBrand());
            pstmt.setDouble(4, car.getPrice());
            pstmt.setString(5, car.getStatus());
            pstmt.setInt(6, car.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createCustomer(Customer customer) {
        String query = "INSERT INTO customers (customer_name, age, gender) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customer.getName());
            pstmt.setInt(2, customer.getAge());
            pstmt.setString(3, customer.getGender());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCustomer(Customer customer, String oldName) {
        String query = "UPDATE customers SET customer_name = ?, age = ?, gender = ? WHERE customer_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customer.getName());
            pstmt.setInt(2, customer.getAge());
            pstmt.setString(3, customer.getGender());
            pstmt.setString(4, oldName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
