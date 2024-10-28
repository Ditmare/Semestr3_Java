public class Car {
    private String type; // "грузовой" или "легковой"
    private String model;
    private String brand;

    public Car(String type, String model, String brand) {
        this.type = type;
        this.model = model;
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    @Override public String toString() {
        return "Тип: " + type + ", Модель: " + model + ", Марка: " + brand;
    }
}