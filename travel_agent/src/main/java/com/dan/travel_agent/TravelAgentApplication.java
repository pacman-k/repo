package com.dan.travel_agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@PropertySource("classpath:telegram_bot_credentials.properties")
public class TravelAgentApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(TravelAgentApplication.class, args);

	}

}
