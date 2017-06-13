package com.bank.user.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bank.user.services.AccountService;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value= "/bill", method = RequestMethod.GET)
	public String payBill(Model model){
		model.addAttribute("accountType", "");
		model.addAttribute("billNumber", "");
		model.addAttribute("amount", "");
		return "bill";
	}
	
	@RequestMapping(value= "/bill", method = RequestMethod.POST)
	public String payBillPost(@ModelAttribute("amount") String amount, @ModelAttribute("billNumber") String billNumber,
			@ModelAttribute("accountType") String accountType, Principal principal){
		accountService.payBill(accountType, Double.parseDouble(amount), principal);
		return "redirect:/userFront";
	}
}
