//package tn.pi.BankApp.web;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.ui.Model;  // ✅ Ajout de l'import
//
//import tn.pi.BankApp.exceptions.UserAlreadyExistsException;
//import tn.pi.BankApp.service.UserService;
//import tn.pi.BankApp.web.dto.UserRegistrationDto;
//
//@Controller
//@RequestMapping("/registration")
//public class UserRegistrationController {
//
//	private UserService userService;
//
//	public UserRegistrationController(UserService userService) {
//		super();
//		this.userService = userService;
//	}
//
//	@ModelAttribute("user")
//	public UserRegistrationDto userRegistrationDto() {
//		return new UserRegistrationDto();
//	}
//
//	@GetMapping
//	public String showRegistrationForm() {
//		return "registration";
//	}
//
//	@PostMapping
//	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, Model model) {
//		try {
//			userService.save(registrationDto);
//			return "redirect:/login?success";
//		} catch (UserAlreadyExistsException ex) {
//			model.addAttribute("error", ex.getMessage());
//			return "registration"; // Recharge la page avec l'erreur
//		}
//	}
//}



//
//package tn.pi.BankApp.web;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import tn.pi.BankApp.exceptions.UserAlreadyExistsException;
//import tn.pi.BankApp.service.UserService;
//import tn.pi.BankApp.web.dto.UserRegistrationDto;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@Controller
//@RequestMapping("/registration")
//public class UserRegistrationController {
//
//	private UserService userService;
//
//	@Value("${upload.directory}")
//	private String uploadDirectory;
//
//	public UserRegistrationController(UserService userService) {
//		this.userService = userService;
//	}
//
//	@ModelAttribute("user")
//	public UserRegistrationDto userRegistrationDto() {
//		return new UserRegistrationDto();
//	}
//
//	@GetMapping
//	public String showRegistrationForm() {
//		return "registration";
//	}
//
//	@PostMapping
//	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, Model model, RedirectAttributes redirectAttributes) {
//		try {
//			// Handle profile picture upload
//			MultipartFile profilePicture = registrationDto.getProfilePicture();
//			if (profilePicture != null && !profilePicture.isEmpty()) {
//				// Save the image in the file system
//				String fileName = profilePicture.getOriginalFilename();
//				Path path = Paths.get(uploadDirectory, fileName);
//				Files.copy(profilePicture.getInputStream(), path);
//
//				// Set the image path in the registrationDto (if needed to store in the database)
//				// You can store only the file name or full path depending on your requirement.
//			}
//
//			// Save the user data
//			userService.save(registrationDto);
//			return "redirect:/login?success";
//
//		} catch (UserAlreadyExistsException ex) {
//			model.addAttribute("error", ex.getMessage());
//			return "registration"; // Reload the registration page with the error message
//		} catch (IOException e) {
//			model.addAttribute("error", "Failed to upload the profile picture.");
//			return "registration";
//		}
//	}
//}


package tn.pi.BankApp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import tn.pi.BankApp.exceptions.UserAlreadyExistsException;
import tn.pi.BankApp.service.UserService;
import tn.pi.BankApp.web.dto.UserRegistrationDto;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

	private final UserService userService;

	public UserRegistrationController(UserService userService) {
		this.userService = userService;
	}

	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}

	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}

	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto,
									  Model model) {
		try {
			userService.save(registrationDto);
			return "redirect:/login?success";
		} catch (UserAlreadyExistsException ex) {
			model.addAttribute("error", ex.getMessage());
			return "registration";
		}
	}
}
