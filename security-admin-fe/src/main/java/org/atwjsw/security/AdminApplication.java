package org.atwjsw.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@RestController
public class AdminApplication {

	@PostMapping("/login")
	public void login(@RequestBody Credentials credentials, HttpServletRequest request) {

		String oauthSeriveUrl = "http://localhost:9070/token/oauth/token";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth("admin", "123456");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("username", credentials.getUsername());
		params.add("password", credentials.getPassword());
		params.add("grant_type", "password");
		params.add("scope", "read write");

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

		ResponseEntity<TokenInfo> response = new RestTemplate()
				.exchange(oauthSeriveUrl, HttpMethod.POST, entity, TokenInfo.class);

		log.info("token info: {}", response.getBody());
		request.getSession().setAttribute("token", response.getBody());
	}

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

}
