package ru.msu.cmc.webprack.DAO.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprack.DAO.ServicesDAO;
import ru.msu.cmc.webprack.models.Services;

import java.util.List;

@Repository
public class ServicesDAOImpl extends CommonDAOImpl<Services, Long> implements ServicesDAO {

    public ServicesDAOImpl() {
        super(Services.class);
    }

    /*@Override
    public List<Services> getServicesByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Services s WHERE lower(s.name) LIKE lower(:name)", Services.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
        }
    }

    @Override
    public List<Services> getServicesByPriceRange(double minPrice, double maxPrice) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Services s WHERE s.price BETWEEN :minPrice AND :maxPrice", Services.class)
                    .setParameter("minPrice", minPrice)
                    .setParameter("maxPrice", maxPrice)
                    .list();
        }
    }

    @Override
    public List<Services> getServicesByDescriptionKeyword(String keyword) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Services s WHERE lower(s.description) LIKE lower(:keyword)", Services.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .list();
        }
    }*/

    @Override
    public List<Services> getAllServices() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Services s", Services.class).list();
        }
    }
}
