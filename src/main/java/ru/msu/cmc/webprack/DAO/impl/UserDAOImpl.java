package ru.msu.cmc.webprack.DAO.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprack.DAO.UserDAO;
import ru.msu.cmc.webprack.models.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDAOImpl extends CommonDAOImpl<Users, Long> implements UserDAO {

    public UserDAOImpl() {
        super(Users.class);
    }

    @Override
    public Users getUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery("FROM Users WHERE name = :username", Users.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }

    /*@Override
    public List<Users> getUsersByRole(String role) {
        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery("FROM Users WHERE role = :role", Users.class)
                    .setParameter("role", role)
                    .list();
        }
    }*/

    /*@Override
    public List<Users> getUsersByEmployeeId(Long employeeId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT e.user FROM Employees e WHERE e.user.id = :employeeId", Users.class)
                    .setParameter("employeeId", employeeId)
                    .list();
        }
    }

    @Override
    public List<Users> getUsersByClientId(Long clientId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT c.user FROM Clients c WHERE c.user.id = :id", Users.class)
                    .setParameter("id", clientId)
                    .list();
        }
    }

    @Override
    public List<Users> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            long count = (Long) session.createQuery("SELECT COUNT(*) FROM Users").getSingleResult();
            List<Users> All = session.createQuery("FROM Users", Users.class).list();
            return All;
        }
    }*/
}
