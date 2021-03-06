package com.hoaxify.hoaxify;

import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.hoaxify.hoaxify.user.User;
import com.hoaxify.hoaxify.user.UserService;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner run(UserService service) {
		return args -> {
			IntStream.rangeClosed(1, 15).mapToObj(i -> {
				User user = new User();
				user.setUsername("user" + i);
				user.setDisplayName("display" + i);
				user.setPassword("P4ssword");
				return user;
			}).forEach(service::save);

		};
	}

}
