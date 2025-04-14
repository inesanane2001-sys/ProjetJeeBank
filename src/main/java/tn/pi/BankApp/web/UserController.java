package tn.pi.BankApp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tn.pi.BankApp.entities.User;
import tn.pi.BankApp.repository.UserRepository;
import java.util.Optional;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository; // Déclaration correcte

    // Injection via le constructeur
    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/logout")
    public String logout() {
        // Optionally clear the security context if you're not using Spring Security
        SecurityContextHolder.clearContext();
        return "redirect:/"; // Redirect to the home page after logout
    }
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "profile";
        } else {
            return "error";
        }
    }
}



