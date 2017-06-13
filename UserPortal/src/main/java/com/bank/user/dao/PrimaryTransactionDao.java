package com.bank.user.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bank.user.domains.transaction.PrimaryTransaction;

public interface PrimaryTransactionDao extends CrudRepository<PrimaryTransaction, Long>{
	
	List<PrimaryTransaction> findAll();

}
