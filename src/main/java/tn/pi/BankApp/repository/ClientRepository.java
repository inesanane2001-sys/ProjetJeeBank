package tn.pi.BankApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.pi.BankApp.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}