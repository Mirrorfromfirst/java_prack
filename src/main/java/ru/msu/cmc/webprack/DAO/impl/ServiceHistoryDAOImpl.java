package ru.msu.cmc.webprack.DAO.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprack.DAO.ServiceHistoryDAO;
import ru.msu.cmc.webprack.models.ServiceHistory;

import java.util.List;

@Repository
public class ServiceHistoryDAOImpl extends CommonDAOImpl<ServiceHistory, Long> implements ServiceHistoryDAO {

    public ServiceHistoryDAOImpl() {
        super(ServiceHistory.class);
    }

    /*@Override
    public List<ServiceHistory> getHistoryByClient(Long clientId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM ServiceHistory sh WHERE sh.client.user.id = :clientId", ServiceHistory.class)
                    .setParameter("clientId", clientId)
                    .list();
        }
    }

    @Override
    public List<ServiceHistory> getHistoryByEmployee(Long employeeId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM ServiceHistory sh WHERE sh.employee.user.id = :employeeId", ServiceHistory.class)
                    .setParameter("employeeId", employeeId)
                    .list();
        }
    }*/
}
