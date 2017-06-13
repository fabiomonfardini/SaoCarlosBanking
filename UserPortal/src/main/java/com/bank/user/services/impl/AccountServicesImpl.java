package com.bank.user.services.impl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.user.dao.PrimaryAccountDao;
import com.bank.user.dao.SavingsAccountDao;
import com.bank.user.domains.User;
import com.bank.user.domains.account.PrimaryAccount;
import com.bank.user.domains.account.SavingsAccount;
import com.bank.user.domains.transaction.PrimaryTransaction;
import com.bank.user.domains.transaction.SavingsTransaction;
import com.bank.user.services.AccountService;
import com.bank.user.services.TransactionService;
import com.bank.user.services.UserService;

@Service
public class AccountServicesImpl implements AccountService{

	private static int nextAccountNumber = 11223145;
	
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	private SavingsAccountDao savingsAccountDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Override
	public PrimaryAccount createPrimaryAccount() {
		
		PrimaryAccount primaryAccount = new PrimaryAccount();
		primaryAccountDao.save(primaryAccount);
		primaryAccount.setAccountBalance(new BigDecimal(0.0));
		primaryAccount.setAccountNumber(accountGen(primaryAccount.getId()));
		
		primaryAccountDao.save(primaryAccount);
		return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
	}

	@Override
	public SavingsAccount createSavingsAccount() {
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccountDao.save(savingsAccount);
		savingsAccount.setAccountBalance(new BigDecimal(0.0));
		savingsAccount.setAccountNumber(accountGen(savingsAccount.getId()));
		
		savingsAccountDao.save(savingsAccount);
		return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
	}

	@Override
	public void deposit(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		
		if("Conta Corrente".equals(accountType)){
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			
			Date date = new Date();
			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposito em Conta Corrente", "Dinheiro", "Finalizado", amount, primaryAccount.getAccountBalance(), primaryAccount);
			transactionService.savePrimaryDepositTransaction(primaryTransaction);
		}
		else if ("Poupanca".equals(accountType)){
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);
			
			Date date = new Date();
			SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposito em Conta Poupanca", "Dinheiro", "Finalizado", amount, savingsAccount.getAccountBalance(), savingsAccount);
			transactionService.saveSavingsDepositTransaction(savingsTransaction);
		}
	}

	@Override
	public void withdraw(String accountType, double amount, Principal principal) {		
		User user = userService.findByUsername(principal.getName());

		if("Conta Corrente".equals(accountType)){
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			Date date = new Date();
			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Saque em Conta Corrente", "Dinheiro", "Finalizado", amount, primaryAccount.getAccountBalance(), primaryAccount);
			transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
		}
		else if ("Poupanca".equals(accountType)){
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();
			SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Saque em Conta Poupanca", "Dinheiro", "Finalizado", amount, savingsAccount.getAccountBalance(), savingsAccount);
			transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
		}
	}
	
	private int accountGen(Long id) {
        return (int) (nextAccountNumber+id);
    }

	@Override
	public boolean accountExists(int accountNumber) {
		PrimaryAccount primaryAccount = primaryAccountDao.findByAccountNumber(accountNumber);
		if (primaryAccount != null){
			return true;
		}
		return false;
	}

	@Override
	public PrimaryAccount findPrimaryAccountbyNumber(String accountNumber) {
		return primaryAccountDao.findByAccountNumber(Integer.parseInt(accountNumber));
	}

	@Override
	public SavingsAccount findSavingsAccountbyNumber(String accountNumber) {
		return savingsAccountDao.findByAccountNumber(Integer.parseInt(accountNumber));
	}

	@Override
	public void payBill(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		if("Conta Corrente".equals(accountType)){
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

			Date date = new Date();
			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Pagamento de Boleto em conta Corrente", "Dinheiro", "Finalizado", amount, primaryAccount.getAccountBalance(), primaryAccount);
			transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
		}
		else if ("Poupanca".equals(accountType)){
			SavingsAccount savingsAccount = user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);

			Date date = new Date();
			SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Pagamento de Boleto com conta poupanca", "Dinheiro", "Finalizado", amount, savingsAccount.getAccountBalance(), savingsAccount);
			transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
		}
		
	}
}
