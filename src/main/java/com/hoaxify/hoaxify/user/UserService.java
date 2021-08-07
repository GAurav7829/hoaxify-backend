package com.hoaxify.hoaxify.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	public User save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User savedUser = userRepository.save(user);
		return savedUser;
	}
	public Page<User> getUsers(User loggedInUser, Pageable pageable) {
		if(loggedInUser!=null) {
			return userRepository.findByUsernameNot(loggedInUser.getUsername(), pageable);
		}
		return userRepository.findAll(pageable);
	}
	
}
