package tn.pi.BankApp.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tn.pi.BankApp.entities.Account;
import tn.pi.BankApp.entities.Operation;
import tn.pi.BankApp.service.AccountService;
import tn.pi.BankApp.service.OperationService;


import java.util.List;

@Controller
@RequestMapping("/operations")
public class OperationController {
    private final OperationService operationService;
    private final AccountService accountService;

    // Constructeur avec injection des dépendances
    public OperationController(OperationService operationService,
                               AccountService accountService) {
        this.operationService = operationService;
        this.accountService = accountService;
    }

    // Afficher le formulaire de dépôt
    @GetMapping("/deposit")
    public String showDepositForm(@RequestParam Long accountId,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            Account account = accountService.getAccountById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Compte introuvable"));
            model.addAttribute("account", account);
            return "operations/deposit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/accounts";
        }
    }

    // Traiter le dépôt
    @PostMapping("/deposit")
    public String processDeposit(@RequestParam Long accountId,
                                 @RequestParam double amount,
                                 RedirectAttributes redirectAttributes) {
        try {
            operationService.deposit(accountId, amount);
            redirectAttributes.addFlashAttribute("success", "Dépôt de " + amount + " effectué avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du dépôt: " + e.getMessage());
        }
        return "redirect:/operations/history?accountId=" + accountId;
    }

    // Afficher le formulaire de retrait
    @GetMapping("/withdraw")
    public String showWithdrawForm(@RequestParam Long accountId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            Account account = accountService.getAccountById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Compte introuvable"));
            model.addAttribute("account", account);
            return "operations/withdraw";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/accounts";
        }
    }

    // Traiter le retrait
    @PostMapping("/withdraw")
    public String processWithdraw(@RequestParam Long accountId,
                                  @RequestParam double amount,
                                  RedirectAttributes redirectAttributes) {
        try {
            operationService.withdraw(accountId, amount);
            redirectAttributes.addFlashAttribute("success", "Retrait de " + amount + " effectué avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du retrait: " + e.getMessage());
        }
        return "redirect:/operations/history?accountId=" + accountId;
    }

    // Afficher l'historique des opérations
    @GetMapping("/history")
    public String showOperationsHistory(@RequestParam Long accountId,
                                        Model model) {
        try {
            Account account = accountService.getAccountById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Compte introuvable"));
            List<Operation> operations = operationService.getAccountOperations(accountId);

            model.addAttribute("account", account);
            model.addAttribute("operations", operations);
            model.addAttribute("balance", account.getBalance()); // Ajout du solde

            return "operations/history";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/accounts";
        }
    }

    // NOUVELLE METHODE AJOUTEE (sans modifier l'existant)
    // Afficher le formulaire de virement
    @GetMapping("/transfer")
    public String showTransferForm(@RequestParam Long accountId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            Account sourceAccount = accountService.getAccountById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Compte source introuvable"));
            model.addAttribute("sourceAccount", sourceAccount);
            return "operations/transfer";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/accounts";
        }
    }

    // NOUVELLE METHODE AJOUTEE (sans modifier l'existant)
    // Traiter le virement
    @PostMapping("/transfer")
    public String processTransfer(@RequestParam Long sourceAccountId,
                                  @RequestParam Long targetAccountId,
                                  @RequestParam double amount,
                                  RedirectAttributes redirectAttributes) {
        try {
            operationService.transfer(sourceAccountId, targetAccountId, amount);
            redirectAttributes.addFlashAttribute("success", "Virement de " + amount + " effectué avec succès");
            return "redirect:/operations/history?accountId=" + sourceAccountId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du virement: " + e.getMessage());
            return "redirect:/operations/transfer?accountId=" + sourceAccountId;
        }
    }
}