package com.mytracker.tracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HelloController {

	@GetMapping("/")
	public String index() {
	System.out.println("HIT HELLO");
	return "Greetings from Spring Boot!";
	}

	// @GetMapping("/")
	// public RedirectView redirectWithUsingRedirectView() {
	// 	RedirectView redirectView = new RedirectView();
	// 	redirectView.setUrl("https://next-website-wilgao09.vercel.app");
	// 	return redirectView;
	// }
}
