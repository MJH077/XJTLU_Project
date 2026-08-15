package com.cpt202.group21;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@SpringBootApplication
public class Group21Application {
	

	public static void main(String[] args) {
		SpringApplication.run(Group21Application.class, args);
	}

	
	@RequestMapping("/")
	public String home() {
    	return "UserRegister"; 
	}
}