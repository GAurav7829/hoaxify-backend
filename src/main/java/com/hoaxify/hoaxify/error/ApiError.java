package com.hoaxify.hoaxify.error;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class ApiError {
	private Long timestamp = new Date().getTime();
	private Integer status;
	private String message;
	private String url;
	private Map<String, String> validationErrors;
	
	public ApiError(Integer status, String message, String url) {
		super();
		this.status = status;
		this.message = message;
		this.url = url;
	}
}
