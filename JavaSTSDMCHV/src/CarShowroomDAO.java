import java.util.List;
import java.util.Map;

public interface CarShowroomDAO {
    void addCar(Car car);
    void sellCar(Car car, Customer customer);
    void createCustomer(Customer customer);
    List<Car> getAllCars();
    List<Customer> getAllCustomers();
    List<Map.Entry<Car, Customer>> getSales();
    void updateCar(Car car);
    void updateCustomer(Customer customer, String oldName);
}
