package tn.pi.BankApp.web;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.pi.BankApp.entities.Client;
import tn.pi.BankApp.service.ClientService;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "clients/list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client invalide ID: " + id)));
        return "clients/form";
    }

    @PostMapping("/save")
    public String saveClient(@ModelAttribute Client client) {
        clientService.saveClient(client);
        return "redirect:/clients";
    }


    @PostMapping("/update")
    public String updateClient(@ModelAttribute Client client) {
        Client existingClient = clientService.findById(client.getId())
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

        // Préserve les relations pour éviter les pertes
        client.setAccounts(existingClient.getAccounts());
        client.setCredits(existingClient.getCredits());
        client.setCartes(existingClient.getCartes());

        clientService.saveClient(client);
        return "redirect:/clients";
    }

    @GetMapping("/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        Client client = clientService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

        // Optionnel : vider les relations avant suppression
        client.getAccounts().clear();
        client.getCredits().clear();
        client.getCartes().clear();

        clientService.deleteClient(id);
        return "redirect:/clients";
    }

}