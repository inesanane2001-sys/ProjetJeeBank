package tn.pi.BankApp.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tn.pi.BankApp.entities.Account;
import tn.pi.BankApp.entities.Client;
import tn.pi.BankApp.service.AccountService;
import tn.pi.BankApp.service.ClientService;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;
    private final ClientService clientService;
    private Client selectedClient;

    @GetMapping("")
    public String listAccounts(@RequestParam Long id, Model model) {
        try {
            selectedClient = clientService.findById(id).get();
            List<Account> accounts = accountService.getAllAccountsByClient(selectedClient);
            logger.debug("Chargement de {} comptes", accounts.size());
            model.addAttribute("accounts", accounts);
            return "accounts/list";
        } catch (Exception e) {
            logger.error("Échec du chargement des comptes", e);
            model.addAttribute("error", "Impossible de charger la liste des comptes");
            return "error";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Account account = new Account();
        account.setType(Account.AccountType.COURANT);
        model.addAttribute("account", account);
        model.addAttribute("typesCompte", Account.AccountType.values());
        model.addAttribute("clients", clientService.getAllClients());
        return "accounts/create-account";
    }

    @PostMapping("/save")
    public String saveAccount(
            @RequestParam Account.AccountType type,
            @RequestParam Long clientId,
            Model model) {

        Account account = new Account();
        account.setAccountNumber(accountService.generateUniqueAccountNumber());
        account.setType(type);
        account.setCreationDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        Client client = clientService.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client invalide ID: " + clientId));

        account.setClient(client);

        accountService.saveAccount(account);

        model.addAttribute("account", account);
        return "accounts/confirmation";
    }

    @GetMapping("/{id}/delete")
    public String deleteAccount(@PathVariable Long id) {
        try {
            logger.info("Suppression du compte ID: {}", id);
            accountService.deleteAccount(id);
            return "redirect:/accounts?success=Compte+supprimé";
        } catch (Exception e) {
            logger.error("Échec de la suppression du compte {}", id, e);
            return "redirect:/accounts?error=Échec+de+la+suppression";
        }
    }

    // NOUVELLES METHODES AJOUTEES (sans modifier l'existant)
    @GetMapping("/{id}/operations")
    public String showOperations(@PathVariable Long id, Model model) {
        try {
            Account account = accountService.getAccountById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Compte introuvable"));
            model.addAttribute("account", account);
            return "accounts/operations";
        } catch (Exception e) {
            logger.error("Erreur affichage opérations", e);
            return "redirect:/accounts?error=Erreur+affichage+opérations";
        }
    }

    @PostMapping("/{id}/deposit")
    public String deposit(@PathVariable Long id,
                          @RequestParam double amount,
                          RedirectAttributes redirectAttributes) {
        try {
            accountService.deposit(id, amount);
            redirectAttributes.addFlashAttribute("success", "Dépôt de " + amount + " effectué");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur dépôt: " + e.getMessage());
        }
        return "redirect:/accounts/" + id + "/operations";
    }

    @PostMapping("/{id}/withdraw")
    public String withdraw(@PathVariable Long id,
                           @RequestParam double amount,
                           RedirectAttributes redirectAttributes) {
        try {
            accountService.withdraw(id, amount);
            redirectAttributes.addFlashAttribute("success", "Retrait de " + amount + " effectué");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur retrait: " + e.getMessage());
        }
        return "redirect:/accounts/" + id + "/operations";
    }
}