package ru.msu.cmc.webprack.DAO.impl;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.webprack.DAO.ClientsDAO;
import ru.msu.cmc.webprack.models.Clients;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class ClientsDAOImpl extends CommonDAOImpl<Clients, Long> implements ClientsDAO {

    public ClientsDAOImpl() {
        super(Clients.class);
    }

    @Override
    public List<Clients> searchClients(String nameOrContactInfo, String serviceName, Date serviceDate) {
        List<Clients> ret = new ArrayList<>();
        for (Clients client : getAll()) {
            if (Objects.equals(client.getName(), serviceName) &&
                    Objects.equals(client.getPhone(), nameOrContactInfo) &&
                    Objects.equals(client.getUser(), serviceDate)) {
                ret.add(client);
            }
        }
        return ret;
    }
}

