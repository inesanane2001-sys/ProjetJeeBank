//package tn.pi.BankApp.web.dto;
//
//public class UserRegistrationDto {
//	private String firstName;
//	private String lastName;
//	private String email;
//	private String password;
//
//	public UserRegistrationDto(){
//
//	}
//
//	public UserRegistrationDto(String firstName, String lastName, String email, String password) {
//		super();
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.email = email;
//		this.password = password;
//	}
//
//	public String getFirstName() {
//		return firstName;
//	}
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//	public String getLastName() {
//		return lastName;
//	}
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//	public String getEmail() {
//		return email;
//	}
//	public void setEmail(String email) {
//		this.email = email;
//	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
//}




package tn.pi.BankApp.web.dto;

import org.springframework.web.multipart.MultipartFile;

public class UserRegistrationDto {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private MultipartFile profilePicture; // Added field for profile picture

	public UserRegistrationDto() {}

	public UserRegistrationDto(String firstName, String lastName, String email, String password, MultipartFile profilePicture) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.profilePicture = profilePicture;
	}

	// Getters and setters for all fields
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public MultipartFile getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(MultipartFile profilePicture) {
		this.profilePicture = profilePicture;
	}
}
