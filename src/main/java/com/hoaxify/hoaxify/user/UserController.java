package com.hoaxify.hoaxify.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.hoaxify.error.ApiError;
import com.hoaxify.hoaxify.shared.CurrentUser;
import com.hoaxify.hoaxify.shared.GenericResponse;
import com.hoaxify.hoaxify.user.vm.UserUpdateVM;
import com.hoaxify.hoaxify.user.vm.UserVM;

@RestController
@RequestMapping("/api/1.0")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/users")
	GenericResponse createUser(@Valid @RequestBody User user) {
		System.out.println("In createUser method");
		userService.save(user);
		return new GenericResponse("User saved");
	}
	
	@GetMapping("/users")
	//Page<UserVM> getUsers(@PageableDefault(size = 10) Pageable pageable) {
	//default provided in application.properties
	Page<UserVM> getUsers(@CurrentUser User loggedInUser, Pageable pageable) {
		//return userService.getUsers().map(user->new UserVM(user));
		//	OR
		return userService.getUsers(loggedInUser, pageable).map(UserVM::new);
	}
	
	@GetMapping("/users/{username}")
	UserVM getUserByName(@PathVariable String username) {
		User user = userService.getByUsername(username);
		return new UserVM(user);
	}
	
	@PutMapping("/users/{id:[0-9]+}")
	@PreAuthorize("#id == principal.id")//added @EnableGlobalMethodSecurity(prePostEnabled = true) to SecurityConfiguration.java
	UserVM updateUser(@PathVariable long id, @RequestBody(required = false) UserUpdateVM user) {
		User updatedUser=userService.update(id,user);
		return new UserVM(updatedUser);
	}
	
	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
		System.out.println("In handleValidationException method");
		ApiError apiError = new ApiError(400,"Validation Error",request.getServletPath());
		BindingResult result = exception.getBindingResult();
		Map<String,String> validationErrors = new HashMap<>();
		for(FieldError error:result.getFieldErrors()) {
			validationErrors.put(error.getField(), error.getDefaultMessage());
		}
		apiError.setValidationErrors(validationErrors);
		return apiError;
	}
	
}