package org.atwjsw.security.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public void login(@Valid UserInfo user, HttpServletRequest request) {
		UserInfo info = userService.login(user);
		// by default, request.getSession will use the existing session if a session
		// is found.
		// request.getSession().setAttribute("user", info);
		// Following logic will always invalidate existing session before creating a new one
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		session = request.getSession(true);
		session.setAttribute("user", info);
	}

	@GetMapping("/logout")
	public void logout(HttpServletRequest request) {
		request.getSession().invalidate();
	}

	@PostMapping
	public UserInfo create(@RequestBody @Validated UserInfo user) {
		return userService.create(user);
	}

	@PutMapping("/{id}")
	public UserInfo update(@RequestBody UserInfo user) {
		return userService.update(user);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		userService.delete(id);
	}

	@GetMapping("/{id}")
	public UserInfo get(@PathVariable Long id, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("user");
		if (user == null || !user.getId().equals(id)) {
			throw new RuntimeException("身份认证信息异常，获取身份认证信息失败");
			// throw new RuntimeException("Authentication fails");
		}
		return userService.get(id);
	}

	@GetMapping
	public List<UserInfo> query(String name) {
		return userService.query(name);
	}

}
