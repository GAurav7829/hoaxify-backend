package com.hoaxify.hoaxify.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.hoaxify.error.ApiError;
import com.hoaxify.hoaxify.shared.CurrentUser;
import com.hoaxify.hoaxify.shared.GenericResponse;
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