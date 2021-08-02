package com.hoaxify.hoaxify.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
	@Autowired
	UserRepository userRepository;
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		User username = userRepository.findByUsername(value);
		if(username==null) {
			return true;
		}
		return false;
	}

}
