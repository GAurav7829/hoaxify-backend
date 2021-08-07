package com.hoaxify.hoaxify.user.vm;

import com.hoaxify.hoaxify.user.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserVM {
	private Long id;
	private String username;
	private String displayName;
	private String image;
	
	public UserVM(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.displayName = user.getDisplayName();
		this.image = user.getImage();
	}
}
