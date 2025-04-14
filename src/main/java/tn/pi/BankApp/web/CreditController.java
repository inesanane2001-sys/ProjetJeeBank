package tn.pi.BankApp.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.pi.BankApp.entities.Account;
import tn.pi.BankApp.entities.Credit;
import tn.pi.BankApp.repository.CreditRepository;
import tn.pi.BankApp.service.AccountService;
import tn.pi.BankApp.service.ClientService;
import tn.pi.BankApp.service.CreditService;


import java.util.List;

@Controller
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {
    private final CreditService creditService;
    private final CreditRepository creditRepository;
    private final ClientService clientService;
    private final AccountService accountService;

    /// type                       element

    @GetMapping("/request")
    public String showRequestForm(@RequestParam Long id,Model model) {
        Credit credit = new Credit();
        credit.setClient(clientService.findById(id).orElseThrow());

        List<Account> accounts = accountService.getAllAccountsByClient(credit.getClient());


        model.addAttribute("credit", credit);
        //construire une variable de type Credit

        model.addAttribute("clientId", id);
//        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("accounts", accounts);
        return "credit/request-form";
    }

    @PostMapping("/submit")
    public String submitRequest(
            @RequestParam Long clientId,
            @RequestParam Long accountId,
            @RequestParam Double salary,
            @RequestParam Double amountRequested,
            Model model ) {
        Credit credit = new Credit();
        credit.setClient(clientService.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client invalide ID: " + clientId)));
        credit.setAccount(accountService.getAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account invalide ID: " + accountId)));
        credit.setSalary(salary);
        credit.setAccountNumber(credit.getAccount().getAccountNumber());
        credit.setEmail(credit.getClient().getEmail());
        credit.setAmountRequested(amountRequested);

        creditService.saveCredit(credit);

        model.addAttribute("credit", credit);

        return "redirect:/credits/confirmation/" + credit.getId();
    }


    @GetMapping("/confirmation/{id}")
    public String showConfirmation(@PathVariable Long id, Model model) {
        Credit credit = creditService.getCreditById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande de crédit invalide"));
        model.addAttribute("credit", credit);
        return "credit/confirmation";
    }

    @GetMapping("/history")
 //   public String showCreditHistory(@RequestParam(required = false) String accountNumber,
 //                                   @RequestParam(required = false) String email,
 //                                   Model model) {
    public String showCreditHistory( Model model) {
//        if (accountNumber != null) {
//            model.addAttribute("credits", creditService.getCreditsByAccount(accountNumber));
//        } else if (email != null) {
//            model.addAttribute("credits", creditService.getCreditsByEmail(email));
//        }
        model.addAttribute("credits", creditService.getAllcredits());
        return "credit/history";
    }

    @GetMapping("/{id}/approve")
    public String approveCredit(@PathVariable Long id,
                                Model model) {
        try {
            Credit credit = creditService.getCreditById(id).get();
            credit.setCreditStatus(Credit.CreditStatus.APPROVED);

            creditService.saveCredit(credit);
//            model.addFlashAttribute("success", "Demande de crédit #" + id + " approuvée avec succès");
        } catch (Exception e) {
//            model.addFlashAttribute("error", "Erreur lors de l'approbation: " + e.getMessage());
        }
        return "redirect:/credits/history";
    }

    @GetMapping("/{id}/reject")
    public String rejectCredit(@PathVariable Long id,
                               Model model) {
        try {
            Credit credit = creditRepository.findById(id).orElseThrow();
            credit.setCreditStatus(Credit.CreditStatus.REJECTED);
            creditService.saveCredit(credit);
//            redirectAttributes.addFlashAttribute("success", "Demande de crédit #" + id + " rejetée avec succès");
        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Erreur lors du rejet: " + e.getMessage());
        }
        return "redirect:/credits/history";
    }
}

