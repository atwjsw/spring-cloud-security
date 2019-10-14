package org.atwjsw.security.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorizationFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		log.info("authorization started");
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();

		if (isNeedAuth(request)) {

			TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
			if (tokenInfo != null && tokenInfo.isActive()) {

				if (!hasPermission(tokenInfo, request)) {
					log.info("audit log update fail 403");
					handleError(403, requestContext);
				}

				requestContext.addZuulRequestHeader("username", tokenInfo.getUser_name());

			} else {

				if (!StringUtils.startsWith(request.getRequestURI(), "/token")) {
					log.info("audit log update fail 401");
					handleError(401, requestContext);
				}
			}

		}

		return null;
	}

	private boolean hasPermission(TokenInfo tokenInfo, HttpServletRequest request) {
		// mock permission check
		// in actual implementation, it should check request uri, httpMethod to evaluate which server
		// the request goes to and what operations it wants to perform, then check if the token has the corresponding
		// resourceIds
		// resource-id (i.e. aud), scopes and authorities
		// return RandomUtils.nextInt() % 2 == 0;
		return true;
	}

	private void handleError(int status, RequestContext requestContext) {
		requestContext.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
		requestContext.setResponseStatusCode(status);
		requestContext.setResponseBody("{\"message\":\"authorization failed\"}");
		// return response
		requestContext.setSendZuulResponse(false);

	}

	private boolean isNeedAuth(HttpServletRequest request) {

		// todo implement logic to check if the request need to be authorized
		return true;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 3;
	}

}
