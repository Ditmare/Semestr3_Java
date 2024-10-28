public class Main {
    public static void main(String[] args) {
        CarShowroom showroom = new CarShowroom();

        Car car1 = new Car("легковой", "ВАЗ 2107", "Лада");
        Car car2 = new Car("грузовой", "43118", "КАМАЗ");
        Car car3 = new Car("грузовой", "Prius", "Toyota");

        showroom.addCar(car1);
        showroom.addCar(car2);
        showroom.addCar(car3);

        showroom.showAllCars();

        Customer customer1 = new Customer("Иванов Иван Иванович", 26, "Мужской");
        Customer customer2 = new Customer("Иванова Анна Ивановга", 25, "Женский");

        showroom.sellCar(car1, customer1);
        showroom.sellCar(car3, customer2);

        showroom.showSales();
        
        showroom.showAllCars();
    }
}
