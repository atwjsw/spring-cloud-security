package org.atwjsw.security.user;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lambdaworks.crypto.SCryptUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserInfo create(UserInfo info) {
		User user = new User();
		BeanUtils.copyProperties(info, user);
		// user.setUsername(null);
		user.setPassword(SCryptUtil.scrypt(info.getPassword(), 32768, 8, 1));
		userRepository.save(user);
		info.setId(user.getId());
		return info;
	}

	@Override
	public UserInfo update(UserInfo user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public UserInfo get(Long id) {
		return userRepository.findById(id).get().buildInfo();
	}

	@Override
	public List<UserInfo> query(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserInfo login(UserInfo info) {
		UserInfo result = null;
		User user = userRepository.findByUsername(info.getUsername());
		if (user != null && SCryptUtil.check(info.getPassword(), user.getPassword())) {
			result = user.buildInfo();
		}
		return result;
	}

}
