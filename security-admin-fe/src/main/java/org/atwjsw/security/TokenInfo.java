package org.atwjsw.security;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TokenInfo {

	private String access_token;
	private String refresh_token;
	private String token_type;
	private Long expires_in;
	private String scope;
	private LocalDateTime expiryTime;

	public boolean isExpired() {
		return expiryTime.isBefore(LocalDateTime.now());
	}

	public TokenInfo init() {
		expiryTime = LocalDateTime.now().plusSeconds(expires_in - 3);
		return this;
	}

}
