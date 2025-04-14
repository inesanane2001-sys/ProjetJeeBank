package tn.pi.BankApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.pi.BankApp.entities.Credit;
import tn.pi.BankApp.repository.CreditRepository;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository creditRepository;


    public Credit saveCredit(Credit credit) {
        return creditRepository.save(credit);
    }

    public Optional<Credit> getCreditById(Long id) {
        return creditRepository.findById(id);
    }


    @Transactional(readOnly = true)
    public List<Credit> getAllcredits() {
        return creditRepository.findAll();
    }


}