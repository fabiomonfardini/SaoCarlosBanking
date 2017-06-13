package com.bank.user.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bank.user.dao.RoleDao;
import com.bank.user.domains.User;
import com.bank.user.domains.account.PrimaryAccount;
import com.bank.user.domains.account.SavingsAccount;
import com.bank.user.domains.security.UserRole;
import com.bank.user.services.UserService;

@Controller
public class HomeController {

	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleDao roleDao;
	
	@RequestMapping("/")
	public String home() {
		return "redirect:/index";
	}

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		User user = new User();
		model.addAttribute("user", user);

		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signupPost(@ModelAttribute("user") User user, Model model) {

		if (userService.checkUserExists(user.getUsername(), user.getEmail())) {
			if (userService.checkEmailExists(user.getEmail())) {
				model.addAttribute("emailExists", true);
			}
			if (userService.checkUsernameExists(user.getUsername())) {
				model.addAttribute("usernameExists", true);
			}
			return "signup";
		} else {
			Set<UserRole> userRole = new HashSet<>();
			
			try {
				userRole.add(new UserRole(user, roleDao.findByName("ROLE_USER")));
			} catch (Exception e) {
				LOG.error("User Role {} not found", "ROLE_USER");
				e.printStackTrace();
			}
			
			userService.create(user, userRole);
			return "redirect:/";
		}
	}

	@RequestMapping("/userFront")
	public String userFront(Principal principal, Model model){
		User user = userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount = user.getPrimaryAccount();
		SavingsAccount savingsAccount = user.getSavingsAccount();
		
		model.addAttribute("primaryAccount", primaryAccount);
		model.addAttribute("savingsAccount", savingsAccount);
		
		return "userFront";
	}
	
}
