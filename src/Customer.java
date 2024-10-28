public class Customer {
    private String fullName;
    private int age;
    private String gender;

    public Customer(String fullName, int age, String gender) {
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    @Override public String toString() {
        return "ФИО: " + fullName + ", Возраст: " + age + ", Пол: " + gender;
    }
}