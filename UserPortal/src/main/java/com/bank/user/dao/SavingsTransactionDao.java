package com.bank.user.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bank.user.domains.transaction.SavingsTransaction;

public interface SavingsTransactionDao extends CrudRepository<SavingsTransaction, Long> {

	List<SavingsTransaction> findAll();
}
