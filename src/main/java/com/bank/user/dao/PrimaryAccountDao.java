package com.bank.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.bank.user.domains.account.PrimaryAccount;

public interface PrimaryAccountDao extends CrudRepository<PrimaryAccount, Long>{

	PrimaryAccount findByAccountNumber(int accountNumber);

}
