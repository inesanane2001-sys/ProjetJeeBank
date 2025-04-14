package tn.pi.BankApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.pi.BankApp.entities.Credit;

public interface CreditRepository  extends JpaRepository<Credit, Long> {
}
