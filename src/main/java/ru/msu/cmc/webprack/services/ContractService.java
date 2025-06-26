package ru.msu.cmc.webprack.services;

public interface ContractService {
    void assignContract(Long contractId, Long employeeId);
    void completeContract(Long contractId);
}
