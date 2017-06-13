package com.bank.user.services;

import java.util.List;
import java.util.Set;

import com.bank.user.domains.User;
import com.bank.user.domains.security.UserRole;

public interface UserService {

	User findByUsername(String username);

	User findByEmail(String email);

	boolean checkUserExists(String username, String email);

	boolean checkUsernameExists(String username);

	boolean checkEmailExists(String email);

	void save(User user);

	User create(User user, Set<UserRole> userRoles);

	User saveUser(User user);

	List<User> findUserList();

	void enableUser(String username);

	void disableUser(String username);
}
