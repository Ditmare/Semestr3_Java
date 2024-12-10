import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CarShowroomController {
    private CarShowroomDAO showroomDAO;

    public CarShowroomController(CarShowroomDAO showroomDAO) {
        this.showroomDAO = showroomDAO;
    }

    public void addCar(Car car) {
        showroomDAO.addCar(car);
    }

    public void sellCar(Car car, Customer customer) {
        showroomDAO.sellCar(car, customer);
    }

    public void createCustomer(Customer customer) {
        showroomDAO.createCustomer(customer);
    }

    public List<Car> getAllCars() {
        return showroomDAO.getAllCars();
    }

    public List<Customer> getAllCustomers() {
        return showroomDAO.getAllCustomers();
    }

    public List<Map.Entry<Car, Customer>> getSales() {
        return showroomDAO.getSales();
    }

    public void updateCar(Car car) {
        showroomDAO.updateCar(car);
    }

    public void updateCustomer(Customer customer, String oldName) {
        showroomDAO.updateCustomer(customer, oldName);
    }

    public void exportToTextFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append("Cars:\n");
        List<Car> cars = showroomDAO.getAllCars();
        for (Car car : cars) {
            sb.append(car.toString()).append("\n");
        }

        sb.append("\nSold Cars:\n"); // Заголовок для проданных автомобилей
        List<Map.Entry<Car, Customer>> sales = showroomDAO.getSales(); // Получаем список проданных автомобилей
        for (Map.Entry<Car, Customer> entry : sales) {
            Car soldCar = entry.getKey();
            Customer customer = entry.getValue();
            sb.append(String.format("Модель: %-15s | Марка: %-10s | Тип: %-10s | Цена: %-10.2f руб. | Статус: %s\n",
                    soldCar.getModel(), soldCar.getBrand(), soldCar.getType(), soldCar.getPrice(), "продан"));
            sb.append(String.format("Покупатель: %-15s | Возраст: %-3d | Пол: %s\n",
                    customer.getName(), customer.getAge(), customer.getGender()));
        }

        sb.append("\nCustomers:\n");
        List<Customer> customers = showroomDAO.getAllCustomers();
        for (Customer customer : customers) {
            sb.append(customer.toString()).append("\n");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
