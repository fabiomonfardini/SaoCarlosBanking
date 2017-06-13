package com.bank.user.services.impl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.user.dao.PrimaryAccountDao;
import com.bank.user.dao.PrimaryTransactionDao;
import com.bank.user.dao.RecipientDao;
import com.bank.user.dao.SavingsAccountDao;
import com.bank.user.dao.SavingsTransactionDao;
import com.bank.user.domains.Recipient;
import com.bank.user.domains.User;
import com.bank.user.domains.account.PrimaryAccount;
import com.bank.user.domains.account.SavingsAccount;
import com.bank.user.domains.transaction.PrimaryTransaction;
import com.bank.user.domains.transaction.SavingsTransaction;
import com.bank.user.services.TransactionService;
import com.bank.user.services.UserService;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private UserService userService;
	
	@Autowired
	private PrimaryTransactionDao primaryTransactionDao;
	
	@Autowired
	private SavingsTransactionDao savingsTransactionDao;
	
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	private SavingsAccountDao savingsAccountDao;
	
	@Autowired
	private RecipientDao recipientDao;
	
	public List<PrimaryTransaction> findPrimaryTransactionList(String username){
		User user = userService.findByUsername(username);
		List<PrimaryTransaction> primaryTransactionList = user.getPrimaryAccount().getPrimaryTransactionList();
		
		return primaryTransactionList;
	}
	
	public List<SavingsTransaction> findSavingsTransactionList(String username){
		User user = userService.findByUsername(username);
		List<SavingsTransaction> savingsTransactionList = user.getSavingsAccount().getSavingsTransactionList();
		
		return savingsTransactionList;
	}
	
	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction){
		primaryTransactionDao.save(primaryTransaction);
	}
	
	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction){
		savingsTransactionDao.save(savingsTransaction);
	}
	
	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction){
		primaryTransactionDao.save(primaryTransaction);
	}
	
	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction){
		savingsTransactionDao.save(savingsTransaction);
	}

	@Override
	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception{
        if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Transferencia entre contas de Conta Corrente para Poupanca", "Transacao", "Finalizado", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
            primaryTransactionDao.save(primaryTransaction);
        } else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Transferencia entre contas de Poupanca para Conta Corrente", "Transacao", "Finalizado", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionDao.save(savingsTransaction);
        } else {
            throw new Exception("Invalid Transfer");
        }
    }

	@Override
	public List<Recipient> findRecipientList(Principal principal) {
		String username = principal.getName();
        List<Recipient> recipientList = recipientDao.findAll().stream() //convert list to stream
                .filter(recipient -> username.equals(recipient.getUser().getUsername()))	//filters the line, equals to username
                .collect(Collectors.toList());

        return recipientList;
	}

	@Override
	public void saveRecipient(Recipient recipient) {
		recipientDao.save(recipient);
	}

	@Override
	public Recipient findRecipientByName(String recipientName) {
		return recipientDao.findByName(recipientName);
	}

	@Override
	public void deleteRecipientByName(String recipientName) {
		recipientDao.deleteByName(recipientName);		
	}

	@Override
	public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {
        if (accountType.equalsIgnoreCase("Conta Corrente")) {
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Transferencia para favorecido "+recipient.getName(), "Transferencia", "Finalizado", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
            primaryTransactionDao.save(primaryTransaction);
        } else if (accountType.equalsIgnoreCase("Poupanca")) {
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Transferencia para favorecido "+recipient.getName(), "Transferencia", "Finalizado", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionDao.save(savingsTransaction);
        }
	}
}
