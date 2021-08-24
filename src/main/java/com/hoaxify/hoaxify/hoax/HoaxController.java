package com.hoaxify.hoaxify.hoax;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.hoaxify.hoax.vm.HoaxVM;
import com.hoaxify.hoaxify.shared.CurrentUser;
import com.hoaxify.hoaxify.user.User;

@RestController
@RequestMapping("/api/1.0")
public class HoaxController {
	@Autowired
	private HoaxService service;
	
	@PostMapping("/hoaxes")
	public HoaxVM createHoax(@Valid @RequestBody Hoax hoax, @CurrentUser User user) {
		return new HoaxVM(service.save(user, hoax));
	}
	
	@GetMapping("/hoaxes")
	public Page<HoaxVM> getAllHoaxes(Pageable pageable) {
		return service.getAllHoaxes(pageable).map(HoaxVM::new);
	}
	
	@GetMapping("/users/{username}/hoaxes")
	public Page<HoaxVM> getHoaxesOfUser(@PathVariable String username, Pageable pageable) {
		return service.getHoaxesOfUser(username,pageable).map(HoaxVM::new);
	}
}
