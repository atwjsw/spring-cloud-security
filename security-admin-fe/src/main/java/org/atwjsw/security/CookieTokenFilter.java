package org.atwjsw.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class CookieTokenFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletResponse response = requestContext.getResponse();

		String accessToken = getCookie("imooc_access_token");
		if (StringUtils.isNotBlank(accessToken)) {
			requestContext.addZuulRequestHeader("Authorization", "Bearer " + accessToken);
		} else {
			String refreshToken = getCookie("imooc_refresh_token");
			if (StringUtils.isNotBlank(refreshToken)) {
				String oauthSeriveUrl = "http://gateway.imooc.com:9070/token/oauth/token";

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				headers.setBasicAuth("admin", "123456");

				MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
				params.add("grant_type", "refresh_token");
				params.add("refresh_token", refreshToken);

				HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

				try {
					ResponseEntity<TokenInfo> newToken = new RestTemplate()
							.exchange(oauthSeriveUrl, HttpMethod.POST, entity, TokenInfo.class);

					requestContext.addZuulRequestHeader("Authorization",
							"Bearer " + newToken.getBody().getAccess_token());

					Cookie accessTokenCookie = new Cookie("imooc_access_token", newToken.getBody().getAccess_token());
					accessTokenCookie.setMaxAge(newToken.getBody().getExpires_in().intValue() - 3);
					accessTokenCookie.setDomain("imooc.com");
					accessTokenCookie.setPath("/");
					response.addCookie(accessTokenCookie);

					Cookie refreshTokenCookie = new Cookie("imooc_refresh_token",
							newToken.getBody().getRefresh_token());
					refreshTokenCookie.setMaxAge(259200);
					refreshTokenCookie.setDomain("imooc.com");
					refreshTokenCookie.setPath("/");
					response.addCookie(refreshTokenCookie);
				} catch (Exception e) {
					log.info("Error refreshing token - http post fails", e);
					requestContext.setSendZuulResponse(false);
					requestContext.setResponseStatusCode(500);
					requestContext.setResponseBody("{\"message\": \"Token refresh failed\"}");
					requestContext.getResponse().setContentType("application/json");
				}
			} else {
				log.info("Error refreshing token - empty token");
				requestContext.setSendZuulResponse(false);
				requestContext.setResponseStatusCode(500);
				requestContext.setResponseBody("{\"message\": \"Token refresh failed\"}");
				requestContext.getResponse().setContentType("application/json");
			}
		}
		return null;
	}

	private String getCookie(String name) {
		String result = null;
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (StringUtils.equals(name, cookie.getName())) {
				result = cookie.getValue();
				break;
			}
		}
		return result;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
