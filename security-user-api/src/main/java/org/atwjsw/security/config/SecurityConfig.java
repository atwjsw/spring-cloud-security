package org.atwjsw.security.config;

import java.util.Optional;

import org.atwjsw.security.filter.AclInterceptor;
import org.atwjsw.security.filter.AuditLogInterceptor;
import org.atwjsw.security.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaAuditing
public class SecurityConfig implements WebMvcConfigurer {

	@Autowired
	private AuditLogInterceptor auditLogInterceptor;

	@Autowired
	private AclInterceptor aclInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(auditLogInterceptor);
		registry.addInterceptor(aclInterceptor);
	}

	@Bean
	public AuditorAware<String> auditorAware() {
		return () -> {
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			UserInfo user = (UserInfo) servletRequestAttributes.getRequest().getSession().getAttribute("user");
			String username = null;
			if (user != null) {
				username = user.getUsername();
			}
			return Optional.ofNullable(username);
		};
	}
}
