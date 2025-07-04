package ru.msu.cmc.webprack.DAO;

import ru.msu.cmc.webprack.models.Clients;
import ru.msu.cmc.webprack.models.Users;

import java.util.Date;
import java.util.List;

public interface ClientsDAO extends CommonDAO<Clients, Long> {
    //List<Clients> searchClients(String nameOrContactInfo, String serviceName, Date serviceDate);
    List<Clients> getAllUsers();

}
