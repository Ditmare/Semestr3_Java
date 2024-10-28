import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarShowroom {
    private List<Car> cars;
    private Map<Car, Customer> sales;

    public CarShowroom() {
        cars = new ArrayList<>();
        sales = new HashMap<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void sellCar(Car car, Customer customer) {
        if (cars.contains(car)) {
            cars.remove(car);
            sales.put(car, customer);
        } else {
            System.out.println("Автомобиль не найден в автосалоне.");
        }
    }

    public void editCarInfo(Car oldCar, Car newCar) {
        if (cars.contains(oldCar)) {
            cars.remove(oldCar);
            cars.add(newCar);
        } else {
            System.out.println("Автомобиль не найден в автосалоне.");
        }
    }

    public void editCustomerInfo(Customer oldCustomer, Customer newCustomer) {
        for (Map.Entry<Car, Customer> entry : sales.entrySet()) {
            if (entry.getValue().equals(oldCustomer)) {
                entry.setValue(newCustomer);
            }
        }
    }

    public void showAllCars() {
        System.out.println("Все автомобили в автосалоне:");
        for (Car car : cars) {
            System.out.println(car);
        }
    }

    public void showSales() {
        System.out.println("Проданные автомобили и покупатели:");
        for (Map.Entry<Car, Customer> entry : sales.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }
}