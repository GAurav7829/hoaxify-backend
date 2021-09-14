package com.hoaxify.hoaxify.hoax;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.hoaxify.hoax.vm.HoaxVM;
import com.hoaxify.hoaxify.shared.CurrentUser;
import com.hoaxify.hoaxify.shared.GenericResponse;
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
		return service.getHoaxesOfUser(username, pageable).map(HoaxVM::new);
	}

	@GetMapping({"/hoaxes/{id:[0-9]+}","/users/{username}/hoaxes/{id:[0-9]+}"})
	public ResponseEntity<?> getHoaxesRelative(@PathVariable long id, 
			@PathVariable(required = false) String username,
			Pageable pageable,
			@RequestParam(name = "direction", defaultValue = "after") String direction,
			@RequestParam(name = "count", defaultValue = "false", required = false) boolean count) {
		if (!direction.equalsIgnoreCase("after")) {
			return ResponseEntity.ok(service.getOldHoaxes(id, username, pageable).map(HoaxVM::new));
		}
		if (count == true) {
			long newHoaxCount = service.getNewHoaxesCount(id, username);
			return ResponseEntity.ok(Collections.singletonMap("count", newHoaxCount));
		}
		List<HoaxVM> newHoaxes = service.getNewHoaxes(id, username, pageable).stream().map(HoaxVM::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(newHoaxes);
	}
	
	@DeleteMapping("/hoaxes/{id:[0-9]+}")
	@PreAuthorize("@hoaxSecurityService.isAllowedToDelete(#id, principal)")
	public GenericResponse deleteHoax(@PathVariable long id) {
		service.deleteHoax(id);
		return new GenericResponse("Hoax is removed");
	}

}
