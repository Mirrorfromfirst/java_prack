package ru.msu.cmc.webprack.DAO.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprack.DAO.ContractsDAO;
import ru.msu.cmc.webprack.models.Contracts;

import java.util.List;

@Repository
public class ContractsDAOImpl extends CommonDAOImpl<Contracts, Long> implements ContractsDAO {

    public ContractsDAOImpl() {
        super(Contracts.class);
    }

    /*@Override
    public List<Contracts> getConractsByClient(Long clientId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Contracts c WHERE c.client.user.id = :clientId", Contracts.class)
                    .setParameter("clientId", clientId)
                    .list();
        }
    }

    @Override
    public List<Contracts> getContractsByService(Long serviceId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Contracts c WHERE c.service.id = :serviceId", Contracts.class)
                    .setParameter("serviceId", serviceId)
                    .list();
        }
    }*/
}
