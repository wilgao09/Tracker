package com.mytracker.tracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

	@GetMapping("/")
	public String index() {
		System.out.println("HIT HELLO");
		return "Greetings from Spring Boot!";
	}

	    @GetMapping("/login")
    public String redirectTo(){
        return "https://williamgao09.com/";
    }

}
