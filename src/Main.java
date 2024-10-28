public class Main {
    public static void main(String[] args) {
        CarShowroom showroom = new CarShowroom();

        Car car1 = new Car("легковой", "ВАЗ 2107", "Лада");
        Car car2 = new Car("грузовой", "43118", "КАМАЗ");
        showroom.addCar(car1);
        showroom.addCar(car2);

        showroom.showAllCars();

        showroom.showSales();

        showroom.showAllCars();
    }
}
