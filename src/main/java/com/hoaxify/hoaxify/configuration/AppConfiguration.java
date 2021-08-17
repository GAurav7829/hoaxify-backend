package com.hoaxify.hoaxify.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "hoaxify")
@Data
public class AppConfiguration {
	
	String uploadPath;//added in application.properties
	String profileImagesFolder = "profile";
	String attachmentsFolder = "attachments";
	
	public String getFullProfileImagePath() {
		return this.uploadPath+"/"+this.profileImagesFolder;
	}
	
	public String getFullAttachmentsPath() {
		return this.uploadPath+"/"+this.attachmentsFolder;
	}
	
}
