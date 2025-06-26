package ru.msu.cmc.webprack.DAO.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprack.DAO.ClientsDAO;
import ru.msu.cmc.webprack.models.Clients;
import ru.msu.cmc.webprack.models.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class ClientsDAOImpl extends CommonDAOImpl<Clients, Long> implements ClientsDAO {

    public ClientsDAOImpl() {
        super(Clients.class);
    }

    /*@Override
    public List<Clients> searchClients(String nameOrContactInfo, String serviceName, Date serviceDate) {
        List<Clients> ret = new ArrayList<>();
        for (Clients client : getAll()) {
            if (Objects.equals(client.getName(), serviceName) &&
                    Objects.equals(client.getPhone(), nameOrContactInfo) ){
                    //Objects.equals(client.getUser(), serviceDate)) {
                ret.add(client);
            }
        }
        return ret;
    }*/

    @Override
    public List<Clients> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            long count = (Long) session.createQuery("SELECT COUNT(*) FROM Clients ").getSingleResult();
            List<Clients> All = session.createQuery("FROM Clients ", Clients.class).list();
            List <Long> ids = session.createQuery("SELECT id FROM Clients ", Long.class).getResultList();
            int iter = 0;
            for (Clients client : All) {
                client.setId(ids.get(iter++));
            }
            session.getTransaction().commit();
            System.out.println(All);
            System.out.println(ids);
            return All;
        }
    }
}

