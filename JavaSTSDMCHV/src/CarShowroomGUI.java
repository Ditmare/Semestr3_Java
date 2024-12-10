import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.io.File;

public class CarShowroomGUI extends JFrame {
    private JTextArea textArea;
    private CarShowroomDAO showroomDAO;

    public CarShowroomGUI(CarShowroomDAO showroomDAO) {
        this.showroomDAO = showroomDAO;
        initialize();
    }

    private void initialize() {
        setTitle("Автосалон");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Центрируем окно на экране

        // стиль для текста
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Информация об автомобилях и покупателях"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 3, 10, 10)); // Две строки, три столбца с отступами
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        // создание кнопок
        JButton sellButton = createButton("Продать автомобиль");
        JButton addCarButton = createButton("Добавить автомобиль");
        JButton addCustomerButton = createButton("Добавить покупателя");
        JButton editCarButton = createButton("Редактировать автомобиль");
        JButton editCustomerButton = createButton("Редактировать покупателя");
        JButton exportTextButton = createButton("Экспорт в текст");

        // действия к кнопкам
        sellButton.addActionListener(e -> sellCar());
        addCarButton.addActionListener(e -> addCar());
        addCustomerButton.addActionListener(e -> addCustomer());
        editCarButton.addActionListener(e -> editCar());
        editCustomerButton.addActionListener(e -> editCustomer());
        exportTextButton.addActionListener(e -> exportDataToText());

        // кнопки на панель
        buttonPanel.add(sellButton);
        buttonPanel.add(addCarButton);
        buttonPanel.add(addCustomerButton);
        buttonPanel.add(editCarButton);
        buttonPanel.add(editCustomerButton);
        buttonPanel.add(exportTextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshList();
        setVisible(true);
    }

    private void exportDataToText() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить как");
        fileChooser.setSelectedFile(new File("export.txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            CarShowroomController controller = new CarShowroomController(showroomDAO);
            controller.exportToTextFile(fileToSave.getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Данные успешно экспортированы в " + fileToSave.getAbsolutePath());
        }
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(new ButtonHoverListener(button)); // Добавляем слушателя для эффекта наведения
        return button;
    }

    private void refreshList() {
        List<Car> cars = showroomDAO.getAllCars();
        List<Map.Entry<Car, Customer>> sales = showroomDAO.getSales();
        List<Customer> customers = showroomDAO.getAllCustomers();

        StringBuilder sb = new StringBuilder();

        // Все автомобили в автосалоне
        sb.append("=============================================================================================\n");
        sb.append("          Все автомобили в автосалоне:\n");
        sb.append("=============================================================================================\n");
        for (Car car : cars) {
            sb.append(String.format("Модель: %-15s | Марка: %-10s | Тип: %-10s | Цена: %-10.2f руб. | Статус: %s\n",
                    car.getModel(), car.getBrand(), car.getType(), car.getPrice(), car.getStatus()));
        }
        sb.append("\n");

        // Проданные автомобили
        sb.append("=============================================================================================\n");
        sb.append("          Проданные автомобили:\n");
        sb.append("=============================================================================================\n");
        for (Map.Entry<Car, Customer> entry : sales) {
            sb.append(String.format("Модель: %-15s | Марка: %-10s | Тип: %-10s | Цена: %-10.2f руб. | Статус: %s\n",
                    entry.getKey().getModel(), entry.getKey().getBrand(), entry.getKey().getType(),
                    entry.getKey().getPrice(), "продан"));
            sb.append(String.format("Покупатель: %-15s | Возраст: %-3d | Пол: %s\n",
                    entry.getValue().getName(), entry.getValue().getAge(), entry.getValue().getGender()));
        }
        sb.append("\n");

        // Все покупатели
        sb.append("=============================================================================================\n");
        sb.append("          Все покупатели:\n");
        sb.append("=============================================================================================\n");
        for (Customer customer : customers) {
            sb.append(String.format("Имя: %-15s | Возраст: %-3d | Пол: %s\n",
                    customer.getName(), customer.getAge(), customer.getGender()));
        }

        textArea.setText(sb.toString());
    }

    private void sellCar() {
        Car selectedCar = getSelectedCar();
        Customer selectedCustomer = getSelectedCustomer();
        if (selectedCar != null && selectedCustomer != null) {
            try {
                showroomDAO.sellCar(selectedCar, selectedCustomer);
                refreshList();
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Пожалуйста, выберите автомобиль и покупателя.");
        }
    }

    private void addCar() {
        JTextField typeField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField brandField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField statusField = new JTextField("доступен");

        Object[] message = {
                "Тип:", typeField,
                "Модель:", modelField,
                "Бренд:", brandField,
                "Цена:", priceField,
                "Статус:", statusField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Добавить автомобиль", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String type = typeField.getText();
                String model = modelField.getText();
                String brand = brandField.getText();
                double price = Double.parseDouble(priceField.getText());
                String status = statusField.getText();

                Car newCar = new Car(0, type, model, brand, price, status);
                showroomDAO.addCar(newCar);
                refreshList();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, введите корректную цену.");
            }
        }
    }

    private void addCustomer() {
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();

        Object[] message = {
                "Имя:", nameField,
                "Возраст:", ageField,
                "Пол:", genderField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Добавить покупателя", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String gender = genderField.getText();

                // Проверка на пустое имя
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Имя не может быть пустым.");
                    return;
                }

                // Проверка на существование покупателя
                List<Customer> customers = showroomDAO.getAllCustomers();
                for (Customer customer : customers) {
                    if (customer.getName().equalsIgnoreCase(name)) {
                        JOptionPane.showMessageDialog(this, "Покупатель с таким именем уже существует.");
                        return;
                    }
                }

                Customer newCustomer = new Customer(name, age, gender);
                showroomDAO.createCustomer(newCustomer);
                refreshList();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, введите корректный возраст.");
            }
        }
    }

    private void editCar() {
        Car selectedCar = getSelectedCar();
        if (selectedCar != null) {
            JTextField typeField = new JTextField(selectedCar.getType());
            JTextField modelField = new JTextField(selectedCar.getModel());
            JTextField brandField = new JTextField(selectedCar.getBrand());
            JTextField priceField = new JTextField(String.valueOf(selectedCar.getPrice()));
            JTextField statusField = new JTextField(selectedCar.getStatus());

            Object[] message = {
                    "Тип:", typeField,
                    "Модель:", modelField,
                    "Бренд:", brandField,
                    "Цена:", priceField,
                    "Статус:", statusField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Редактировать автомобиль", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    selectedCar.setType(typeField.getText());
                    selectedCar.setModel(modelField.getText());
                    selectedCar.setBrand(brandField.getText());
                    selectedCar.setPrice(Double.parseDouble(priceField.getText()));
                    selectedCar.setStatus(statusField.getText());
                    showroomDAO.updateCar(selectedCar);
                    refreshList();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Пожалуйста, введите корректную цену.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Пожалуйста, выберите автомобиль для редактирования.");
        }
    }

    private void editCustomer() {
        Customer selectedCustomer = getSelectedCustomer();
        if (selectedCustomer != null) {
            JTextField nameField = new JTextField(selectedCustomer.getName());
            JTextField ageField = new JTextField(String.valueOf(selectedCustomer.getAge()));
            JTextField genderField = new JTextField(selectedCustomer.getGender());

            Object[] message = {
                    "Имя:", nameField,
                    "Возраст:", ageField,
                    "Пол:", genderField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Редактировать покупателя", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String oldName = selectedCustomer.getName(); // Сохраняем старое имя
                    selectedCustomer.setName(nameField.getText());
                    selectedCustomer.setAge(Integer.parseInt(ageField.getText()));
                    selectedCustomer.setGender(genderField.getText());
                    showroomDAO.updateCustomer(selectedCustomer, oldName); // Передаем старое имя
                    refreshList();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Пожалуйста, введите корректный возраст.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Пожалуйста, выберите покупателя для редактирования.");
        }
    }

    private Car getSelectedCar() {
        List<Car> cars = showroomDAO.getAllCars();
        String[] carOptions = cars.stream().map(Car::toString).toArray(String[]::new);

        String selectedCarString = (String) JOptionPane.showInputDialog(
                this,
                "Выберите автомобиль:",
                "Выбор автомобиля",
                JOptionPane.QUESTION_MESSAGE,
                null,
                carOptions,
                carOptions[0]
        );

        if (selectedCarString != null) {
            for (Car car : cars) {
                if (car.toString().equals(selectedCarString)) {
                    return car;
                }
            }
        }
        return null;
    }

    private Customer getSelectedCustomer() {
        List<Customer> customers = showroomDAO.getAllCustomers();
        String[] customerOptions = customers.stream().map(Customer::toString).toArray(String[]::new);

        String selectedCustomerString = (String) JOptionPane.showInputDialog(
                this,
                "Выберите покупателя:",
                "Выбор покупателя",
                JOptionPane.QUESTION_MESSAGE,
                null,
                customerOptions,
                customerOptions[0]
        );

        if (selectedCustomerString != null) {
            for (Customer customer : customers) {
                if (customer.toString().equals(selectedCustomerString)) {
                    return customer;
                }
            }
        }
        return null;
    }

    private class ButtonHoverListener implements ActionListener {
        private JButton button;

        public ButtonHoverListener(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.YELLOW);
        }
    }
}
