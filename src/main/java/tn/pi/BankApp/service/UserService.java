package tn.pi.BankApp.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import tn.pi.BankApp.entities.User;
import tn.pi.BankApp.web.dto.UserRegistrationDto;
import tn.pi.BankApp.exceptions.UserAlreadyExistsException;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService {

	User save(UserRegistrationDto registrationDto) throws UserAlreadyExistsException;
	User getCurrentUser();
	User findByEmail(String email);

}
