package com.lancesoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class Osmdummy1Application {

	public static void main(String[] args) {
		SpringApplication.run(Osmdummy1Application.class, args);
	}

	
}
