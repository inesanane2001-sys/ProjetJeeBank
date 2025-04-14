package tn.pi.BankApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.pi.BankApp.entities.Carte;

@Repository
public interface CarteRepository extends JpaRepository<Carte, Long> {
    boolean existsByCarteNumber(String carteNumber);

}