package com.bank.user.services;

import java.security.Principal;
import java.util.List;

import com.bank.user.domains.Recipient;
import com.bank.user.domains.account.PrimaryAccount;
import com.bank.user.domains.account.SavingsAccount;
import com.bank.user.domains.transaction.PrimaryTransaction;
import com.bank.user.domains.transaction.SavingsTransaction;

public interface TransactionService {

	public List<PrimaryTransaction> findPrimaryTransactionList(String username);

	public List<SavingsTransaction> findSavingsTransactionList(String username);

	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);

	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);

	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);

	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction);

	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, PrimaryAccount primaryAccount,
			SavingsAccount savingsAccount) throws Exception;

	public List<Recipient> findRecipientList(Principal principal);

	public void saveRecipient(Recipient recipient);

	public Recipient findRecipientByName(String recipientName);

	public void deleteRecipientByName(String recipientName);

	public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount);
}
