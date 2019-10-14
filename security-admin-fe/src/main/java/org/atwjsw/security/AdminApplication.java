package org.atwjsw.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@RestController
@EnableZuulProxy
public class AdminApplication {

	@GetMapping("/oauth/callback")
	public void me(@RequestParam String code, String state,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		log.info("state is {}", state);

		String oauthSeriveUrl = "http://localhost:9070/token/oauth/token";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth("admin", "123456");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", code);
		params.add("grant_type", "authorization_code");
		params.add("redirect_uri", "http://admin.imooc.com:8080/oauth/callback");

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

		ResponseEntity<TokenInfo> token = new RestTemplate()
				.exchange(oauthSeriveUrl, HttpMethod.POST, entity, TokenInfo.class);
		request.getSession().setAttribute("token", token.getBody());
		log.info("token info: {}", token.getBody());
		response.sendRedirect("/");
	}

	@GetMapping("/me")
	public TokenInfo me(HttpServletRequest request) {
		TokenInfo info = (TokenInfo) request.getSession().getAttribute("token");
		log.info("/me token: {}", info);
		return info;
	}

	@PostMapping("/logout")
	public void logout(HttpServletRequest request) {
		request.getSession().invalidate();
	}

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

}
