import dao.EmployeeDao;
import dao.impl.EmployeeDaoImpl;
import model.City;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Application {
    public static final String USER = "postgres";
    public static final String PASSWORD = "Safarian.8259";
    public static final String URL = "jdbc:postgresql://localhost:5432/skypro";
    public static final Connection connection;
    public static EmployeeDao employeeDao;

    static {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee WHERE id = 4")) {

            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("first_name: " + resultSet.getString(2));
                System.out.println("last_name: " + resultSet.getString(3));
                System.out.println("gender: " + resultSet.getString(4));
                System.out.println("city_id: " + resultSet.getInt(6));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    static {
        try {
            employeeDao = new EmployeeDaoImpl(connection);
            City city1 = new City(1, "Kursk");
            City city2 = new City(2, "Belgorod");
            City city3 = new City(3, "Moskva");
            Employee employee1 = new Employee("Sveta", "Polokhova", "f", 33, city1);
            Employee employee2 = new Employee("Vanya", "Kirov", "m", 42, city2);
            Employee employee3 = new Employee("Viki", "Morozova", "f", 25, city3);

//            Добавить сотрудников
            employeeDao.create(employee1);
            employeeDao.create(employee2);
            employeeDao.create(employee3);
            System.out.println("Получить всех сотрудников");
            List<Employee> list = new ArrayList<>(employeeDao.readAll());
            for (Employee employee : list) {
                System.out.println(employee);
            }

//           Изменить по ID имя
            employeeDao.updateById(3, "Elena");

            System.out.println("Прочитать по ID");
            employeeDao.readById(10);
//            Удалить по ID
            employeeDao.deleteById(77);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
