package com.bank.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.bank.user.domains.security.Role;

public interface RoleDao extends CrudRepository<Role, Long>{
	Role findByName(String name);
}
