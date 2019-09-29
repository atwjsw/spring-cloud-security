package org.atwjsw.security.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atwjsw.security.log.AuditLog;
import org.atwjsw.security.log.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AuditLogInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private AuditLogRepository auditLogRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		System.out.println("3.1");

		AuditLog log = new AuditLog();
		log.setMethod(request.getMethod());
		log.setPath(request.getRequestURI());
		// User user = (User) request.getAttribute("user");
		// if (user != null) {
		// log.setUsername(user.getUsername());
		// }
		auditLogRepository.save(log);
		request.setAttribute("auditLogId", log.getId());

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		System.out.println("3.2");

		Long audiLogId = (Long) request.getAttribute("auditLogId");

		AuditLog log = auditLogRepository.findById(audiLogId).get();

		log.setStatus(response.getStatus());

		auditLogRepository.save(log);

	}

}
