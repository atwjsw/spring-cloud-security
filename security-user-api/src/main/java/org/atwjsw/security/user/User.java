package org.atwjsw.security.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@NotBlank(message = "用户名不能为空")
	@Column(unique = true)
	private String username;

	private String password;

	private String permissions;

	public UserInfo buildInfo() {
		UserInfo info = new UserInfo();
		BeanUtils.copyProperties(this, info);
		return info;
	}

}
