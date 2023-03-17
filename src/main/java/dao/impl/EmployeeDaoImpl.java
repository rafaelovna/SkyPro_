package dao.impl;

import dao.EmployeeDao;
import model.City;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

//    public final String USER = "postgres";
//    public final String PASSWORD = "Safarian.8259";
//    public final String URL = "jdbc:postgresql://localhost:5432/skypro";= DriverManager.getConnection(URL, USER, PASSWORD)

    public Connection connection;


    public EmployeeDaoImpl(Connection connection) throws SQLException{
        this.connection = connection;
    }



    @Override
    public void create(Employee employee) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO employee (first_name, last_name, gender, age, city_id) VALUES ((?), (?), (?), (?),(?))")) {

            statement.setString(1, employee.getFirst_name());
            statement.setString(2, employee.getLast_name());
            statement.setString(3, employee.getGender());
            statement.setInt(4, employee.getAge());
            statement.setInt(5, employee.getCity_id().getCity_id());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee readById(int id) {
        Employee employee = new Employee();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM employee INNER JOIN city ON employee.city_id = city.city_id AND id = (?)")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                employee.setId(Integer.parseInt(resultSet.getString("id")));
                employee.setFirst_name(resultSet.getString("first_name"));
                employee.setLast_name(resultSet.getString("last_name"));
                employee.setGender(resultSet.getString("gender"));
                employee.setAge(Integer.parseInt(resultSet.getString("age")));
                employee.setCity_id(new City(resultSet.getInt("city_id"), resultSet.getString("city_name")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(employee);
        return employee;
    }

    @Override
    public List<Employee> readAll() {

        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM employee INNER JOIN city ON employee.city_id = city.city_id")){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = Integer.parseInt(resultSet.getString("id"));
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String gender = resultSet.getString("gender");
                int age = Integer.parseInt(resultSet.getString("age"));
                City city = new City(resultSet.getInt("city_id"), resultSet.getString("city_name"));
                Employee employee = new Employee(id, first_name, last_name, gender, age, city);
                employees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employees;
    }

    @Override
    public void updateById(int id, String first_name) {
        try(PreparedStatement statement = connection.prepareStatement(
                "UPDATE employee SET first_name = (?) WHERE id = (?)")) {
            statement.setString(1, first_name);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(int id) {
        try(PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM employee WHERE id = (?)")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
