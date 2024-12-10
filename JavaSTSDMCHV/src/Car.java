public class Car {
    private int id;
    private String type;
    private String model;
    private String brand;
    private double price;
    private String status;

    public Car(int id, String type, String model, String brand, double price, String status) {
        this.id = id;
        this.type = type;
        this.model = model;
        this.brand = brand;
        this.price = price;
        this.status = status;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getModel() { return model; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setModel(String model) { this.model = model; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setPrice(double price) { this.price = price; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
