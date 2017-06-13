package com.bank.user.services;

import java.security.Principal;

import com.bank.user.domains.account.PrimaryAccount;
import com.bank.user.domains.account.SavingsAccount;

public interface AccountService {

	PrimaryAccount createPrimaryAccount();
	SavingsAccount createSavingsAccount();
	
	void deposit(String accountType, double amount, Principal principal);
	void withdraw(String accountType, double amount, Principal principal);
	boolean accountExists(int accountNumber);
	PrimaryAccount findPrimaryAccountbyNumber(String accountNumber);
	SavingsAccount findSavingsAccountbyNumber(String accountNumber);
	void payBill(String accountType, double amount, Principal principal);
}
