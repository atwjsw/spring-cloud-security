package org.atwjsw.security.user;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaSpecificationExecutor<User>, CrudRepository<User, Long> {

	User findByUsername(String username);

}
