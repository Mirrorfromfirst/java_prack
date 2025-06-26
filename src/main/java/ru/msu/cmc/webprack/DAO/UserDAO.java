package ru.msu.cmc.webprack.DAO;

import ru.msu.cmc.webprack.models.Users;

import java.util.List;

public interface UserDAO extends CommonDAO<Users, Long>{
    Users getUserByUsername(String username);
    List<Users> getUsersByRole(String role);
    List<Users> getUsersByEmployeeId(Long employeeId);
    List<Users> getUsersByClientId(Long clientId);
}
