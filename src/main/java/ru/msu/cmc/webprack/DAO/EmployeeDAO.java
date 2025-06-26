package ru.msu.cmc.webprack.DAO;

import ru.msu.cmc.webprack.models.Employees;

import java.util.List;

public interface EmployeeDAO extends CommonDAO<Employees, Long> {
    List<Employees> getEmployeesByPosition(String position);

    List<Employees> getEmployeesByEducation(String education);

}

