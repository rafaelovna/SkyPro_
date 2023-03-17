package dao;

import model.Employee;

import java.util.List;

public interface EmployeeDao {

    void create(Employee employee);

    Employee readById(int id);

    List<Employee> readAll();

    void updateById(int id, String first_name);

    void deleteById(int id);
}
