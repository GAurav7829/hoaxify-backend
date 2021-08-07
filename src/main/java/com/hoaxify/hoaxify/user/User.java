package com.hoaxify.hoaxify.user;

import java.beans.Transient;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
@Entity
public class User implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9103072629979540430L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull(message = "{hoaxify.constraints.username.NotNull.message}")
	@Size(min = 4, max = 255)
	@UniqueUsername
	private String username;
	@NotNull
	@Size(min = 4, max = 255)
	private String displayName;
	@NotNull
	@Size(min = 8, max = 255)
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{hoaxify.constraints.password.Pattern.message}")
	private String password;
	
	private String image;
	
	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("Role_USER");
	}
	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	@Transient
	public boolean isEnabled() {
		return true;
	}
}
