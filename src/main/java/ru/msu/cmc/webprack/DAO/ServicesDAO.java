package ru.msu.cmc.webprack.DAO;

import ru.msu.cmc.webprack.models.Services;

import java.util.List;

public interface ServicesDAO extends CommonDAO<Services, Long> {
    //List<Services> getServicesByName(String name);
    //List<Services> getServicesByPriceRange(double minPrice, double maxPrice);
    //List<Services> getServicesByDescriptionKeyword(String keyword);
    List<Services> getAllServices();
}
