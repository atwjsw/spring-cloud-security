package org.atwjsw.security.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuthFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		log.info("oauth started");

		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();

		// allows the request pass the filter if request is directed to Authorization server
		if (StringUtils.startsWith(request.getRequestURI(), "/token")) {
			return null;
		}

		String authHeader = request.getHeader("Authorization");

		// allows the request pass the filter even if header Authorization not exists.
		// but no UserInfo is placed into request
		// which means in later Authorization Filter, the request will be rejected
		if (StringUtils.isBlank(authHeader)) {
			return null;
		}

		// if Authorization header is not bearer token, same as above
		if (!StringUtils.startsWith(authHeader, "Bearer ")) {
			return null;
		}

		// get tokenInfo from Authrozation Server and place it into request for Authorization filter
		try {
			TokenInfo info = getTokenInfo(authHeader);
			request.setAttribute("tokenInfo", info);
		} catch (Exception e) {
			log.error("Get token info fails.");
		}

		return null;

	}

	private TokenInfo getTokenInfo(String authHeader) {
		String token = StringUtils.substringAfter(authHeader, "Bearer ");
		String oauthSeriveUrl = "http://localhost:9090/oauth/check_token";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth("gateway", "123456");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("token", token);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

		ResponseEntity<TokenInfo> response = new RestTemplate()
				.exchange(oauthSeriveUrl, HttpMethod.POST, entity, TokenInfo.class);

		log.info("token info: {}", response.getBody());

		return response.getBody();
	}

	@Override
	public String filterType() {
		return "pre"; // pre, post, error, route
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
