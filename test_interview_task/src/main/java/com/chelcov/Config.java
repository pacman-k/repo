package com.chelcov;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.PrintStream;
import java.text.SimpleDateFormat;

@Configuration
@ComponentScan
@PropertySource("classpath:props.properties")
public class Config {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().setDateFormat(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"));
    }

    @Bean
    public PrintStream printStream() {
        return System.out;
    }

}
