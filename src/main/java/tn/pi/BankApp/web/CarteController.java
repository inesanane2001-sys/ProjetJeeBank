package tn.pi.BankApp.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.pi.BankApp.entities.Account;
import tn.pi.BankApp.entities.Carte;
import tn.pi.BankApp.service.AccountService;
import tn.pi.BankApp.service.CarteService;
import tn.pi.BankApp.service.ClientService;


import java.util.List;


@Controller
@RequestMapping("/cartes")
@RequiredArgsConstructor
public class CarteController {
    private static final Logger logger = LoggerFactory.getLogger(CarteController.class);

    private final CarteService carteService;
    private final ClientService clientService;
    private final AccountService accountService;
    private Account selectedAccount;

    @GetMapping("")
    public String listAccounts(@RequestParam Long id, Model model) {
        try {
            if ( accountService.getAccountById(id).isPresent()){
                selectedAccount = accountService.getAccountById(id).orElseThrow();
                List<Carte> cartes = carteService.getAllCartesByAccount(selectedAccount);
            } else {
                List<Carte> cartes = carteService.getAllCartes();
            }
            List<Carte> cartes = carteService.getAllCartesByAccount(selectedAccount);
            model.addAttribute("cartes", cartes);
            model.addAttribute("clientId", id);
            return "cartes/list-carte";
        } catch (Exception e) {
            logger.error("Échec du chargement des cartes", e);
            model.addAttribute("error", "Impossible de charger la liste des comptes");
            return "error";
        }
    }

    @GetMapping("/add")
    public String showRequestForm(@RequestParam Long clientId, Model model) {
        Carte carte = new Carte();
        carte.setClient(clientService.findById(clientId).orElseThrow());
        String  carteNumber = carteService.generateUniqueCarteNumber();

        List<Account> accounts = accountService.getAllAccountsByClient(carte.getClient());

        model.addAttribute("accounts", accounts);
        model.addAttribute("carteNumber", carteNumber);
        model.addAttribute("clientId", clientId);
        model.addAttribute("typesCarte", Carte.typesCarte.values());
        return "cartes/create-carte";
    }

    @PostMapping("/submit")
    public String submitRequest(
            @RequestParam Carte.typesCarte type,
            @RequestParam String carteNumber,
            @RequestParam Long accountId,
            @RequestParam Long clientId,
            Model model ) {
        Carte carte = new Carte();

        carte.setClient(clientService.findById(clientId).orElseThrow());
        carte.setCarteNumber(carteNumber);
        carte.setType(type);
        carte.setAccount(accountService.getAccountById(accountId).orElseThrow());
        carte.setAccountNumber(carte.getAccount().getAccountNumber());
        carteService.saveCarte(carte);

        model.addAttribute("carte", carte);

        return "redirect:/cartes/confirmation/" + carte.getId();
    }

    @GetMapping("/confirmation/{id}")
    public String showConfirmation(@PathVariable Long id, Model model) {
        Carte carte = carteService.getCarteById(id);
        model.addAttribute("carte", carte);
        return "cartes/confirmation";
    }


}