package com.bank.user.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bank.user.dao.PrimaryAccountDao;
import com.bank.user.dao.SavingsAccountDao;
import com.bank.user.domains.Recipient;
import com.bank.user.domains.User;
import com.bank.user.domains.account.PrimaryAccount;
import com.bank.user.domains.account.SavingsAccount;
import com.bank.user.services.AccountService;
import com.bank.user.services.TransactionService;
import com.bank.user.services.UserService;

@Controller
@RequestMapping("/transfer")
public class TransferController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired 
	private AccountService accountService;
	
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	private SavingsAccountDao savingsAccountDao;
	
	@RequestMapping(value= "/betweenAccounts", method = RequestMethod.GET)
	public String betweenAccounts(Model model){
		model.addAttribute("transferFrom", "");
		model.addAttribute("transferTo", "");
		model.addAttribute("amount", "");
		
		return "betweenAccounts";
	}
	
	@RequestMapping(value= "/betweenAccounts", method = RequestMethod.POST)
	public String betweenAccountsPOST(
			@ModelAttribute("transferFrom") String transferFrom,
			@ModelAttribute("transferTo") String transferTo,
			@ModelAttribute("amount") String amount,
			Principal principal){
		User user =  userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		SavingsAccount savingsAccount = user.getSavingsAccount();
		
		try {
			transactionService.betweenAccountsTransfer(transferFrom, transferTo, amount, primaryAccount, savingsAccount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/userFront";
	}
	
	@RequestMapping(value = "/recipient", method = RequestMethod.GET)
    public String recipient(Model model, Principal principal) {
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        Recipient recipient = new Recipient();

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        return "recipient";
    }

    @RequestMapping(value = "/recipient/save", method = RequestMethod.POST)
    public String recipientPost(@ModelAttribute("recipient") Recipient recipient, Principal principal, Model model) {
    	String accountNumber = recipient.getAccountNumber();
    	
    	if(StringUtils.isEmpty(accountNumber) || !accountService.accountExists(Integer.parseInt(accountNumber))){
    		model.addAttribute("accountDoesNotExist", true);
    		return "recipient";
    	}
    	
        User user = userService.findByUsername(principal.getName());
        recipient.setUser(user);
        transactionService.saveRecipient(recipient);

        return "redirect:/transfer/recipient";
    }

    @RequestMapping(value = "/recipient/edit", method = RequestMethod.GET)
    public String recipientEdit(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){

        Recipient recipient = transactionService.findRecipientByName(recipientName);
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        return "recipient";
    }

    @RequestMapping(value = "/recipient/delete", method = RequestMethod.GET)
    @Transactional
    public String recipientDelete(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){

        transactionService.deleteRecipientByName(recipientName);

        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        Recipient recipient = new Recipient();
        model.addAttribute("recipient", recipient);
        model.addAttribute("recipientList", recipientList);

        return "recipient";
    }
    
    @RequestMapping(value = "/toSomeoneElse",method = RequestMethod.GET)
    public String toSomeoneElse(Model model, Principal principal) {
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("accountType", "");

        return "toSomeoneElse";
    }

    @RequestMapping(value = "/toSomeoneElse",method = RequestMethod.POST)
    public String toSomeoneElsePost(@ModelAttribute("recipientName") String recipientName, @ModelAttribute("accountType") String accountType, @ModelAttribute("amount") String amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Recipient recipient = transactionService.findRecipientByName(recipientName);
        
        if("Conta Corrente".equals(accountType)){
        	PrimaryAccount recipientPrimary = accountService.findPrimaryAccountbyNumber(recipient.getAccountNumber());
        	BigDecimal balance = recipientPrimary.getAccountBalance();
        	recipientPrimary.setAccountBalance(balance.add(new BigDecimal(Double.parseDouble(amount))));
        	primaryAccountDao.save(recipientPrimary);
		}
		else if ("Poupanca".equals(accountType)){
			SavingsAccount recipientSavings = accountService.findSavingsAccountbyNumber(recipient.getAccountNumber());
        	BigDecimal balance = recipientSavings.getAccountBalance();
        	recipientSavings.setAccountBalance(balance.add(new BigDecimal(Double.parseDouble(amount))));
        	savingsAccountDao.save(recipientSavings);
		}
        transactionService.toSomeoneElseTransfer(recipient, accountType, amount, user.getPrimaryAccount(), user.getSavingsAccount());
        return "redirect:/userFront";
    }
}
