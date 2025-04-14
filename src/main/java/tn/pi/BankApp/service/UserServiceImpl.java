package tn.pi.BankApp.service;


import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import tn.pi.BankApp.exceptions.UserAlreadyExistsException;
import tn.pi.BankApp.entities.Role;
import tn.pi.BankApp.entities.User;
import tn.pi.BankApp.repository.UserRepository;
import tn.pi.BankApp.web.dto.UserRegistrationDto;



@Service
public class UserServiceImpl implements UserService{

	private UserRepository userRepository;

	private String uploadPath;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public User save(UserRegistrationDto registrationDto) {
		// Vérifier si l'utilisateur existe déjà
		if (userRepository.existsByEmail(registrationDto.getEmail())) {
			throw new UserAlreadyExistsException("Email déjà utilisé !");
		}

		User user = new User(
				registrationDto.getFirstName(),
				registrationDto.getLastName(),
				registrationDto.getEmail(),
				passwordEncoder.encode(registrationDto.getPassword()),
				Arrays.asList(new Role("ROLE_USER"))
		);

		return userRepository.save(user);
	}

	@Override
	public User getCurrentUser() {
		String email = getAuthenticatedUsername();
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));
	}

	private String getAuthenticatedUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		} else {
			return principal.toString();
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				mapRolesToAuthorities(user.getRoles())
		);
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
