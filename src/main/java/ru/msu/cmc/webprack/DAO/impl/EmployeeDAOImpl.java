package ru.msu.cmc.webprack.DAO.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprack.DAO.EmployeeDAO;
import ru.msu.cmc.webprack.models.Employees;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeDAOImpl extends CommonDAOImpl<Employees, Long> implements EmployeeDAO {

    public EmployeeDAOImpl() {
        super(Employees.class);
    }

    /*@Override
    public List<Employees> getEmployeesByPosition(String position) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Employees e WHERE lower(e.position) LIKE lower(:position)", Employees.class)
                    .setParameter("position", "%" + position + "%")
                    .list();
        }
    }

    @Override
    public List<Employees> getEmployeesByEducation(String education) {
        return new ArrayList<>();
        // try (Session session = sessionFactory.openSession()) {
          //  return session.createQuery(
            //                "FROM Employees e WHERE lower(e.) LIKE lower(:education)", Employees.class)
              //      .setParameter("education", "%" + education + "%")
                //    .list();
       // }
    }*/

    @Override
    public List<Employees> getAllEmployees() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Employees", Employees.class).list();
        }
    }
}
