package com.bank.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.bank.user.domains.account.SavingsAccount;

public interface SavingsAccountDao extends CrudRepository<SavingsAccount, Long>{

	SavingsAccount findByAccountNumber(int accountNumber);

}
