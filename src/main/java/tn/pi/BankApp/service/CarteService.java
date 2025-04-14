package tn.pi.BankApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.pi.BankApp.entities.Account;
import tn.pi.BankApp.entities.Carte;
import tn.pi.BankApp.repository.CarteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CarteService {
    private final CarteRepository carteRepository;
    private static final Random RANDOM = new Random();

    public Carte saveCarte(Carte carte) {
        return carteRepository.save(carte);
    }

    public Carte createCarte(Carte carte) {
        return carteRepository.save(carte);
    }

    public Carte getCarteById(Long id) {
        return carteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carte not found with id: " + id));
    }

    public List<Carte> getAllCartes() {
        return carteRepository.findAll();
    }

    public void deleteCarte(Long id) {
        Carte carte = getCarteById(id);
        carteRepository.delete(carte);
    }

    public List<Carte> getAllCartesByAccount(Account account) {
        List<Carte> allCartes = carteRepository.findAll();
        List<Carte> accountCartes = new ArrayList<>();
        for (Carte carte : allCartes){
            if (account.getId().equals(carte.getAccount().getId())){
                accountCartes.add(carte);
            }
        }
        return accountCartes;
    }

    public String generateUniqueCarteNumber() {
        String carteNumber;
        do {
            carteNumber = generateRandomCarteNumber();
        } while (carteRepository.existsByCarteNumber(carteNumber));

        return carteNumber;
    }
    private String generateRandomCarteNumber() {
        // 9-digit number with leading zeros if needed
        return String.format("%09d", RANDOM.nextInt(1_000_000_000));
    }
}
