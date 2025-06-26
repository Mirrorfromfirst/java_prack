package ru.msu.cmc.webprack.DAO;

import ru.msu.cmc.webprack.models.ServiceHistory;

import java.util.List;

public interface ServiceHistoryDAO extends CommonDAO<ServiceHistory, Long>{
    List<ServiceHistory> getHistoryByClient(Long clientId);
    List<ServiceHistory> getHistoryByEmployee(Long employeeId);
}
