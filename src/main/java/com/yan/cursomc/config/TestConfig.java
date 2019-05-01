package com.yan.cursomc.config;

import java.text.ParseException;

import com.yan.cursomc.services.DBService;
import com.yan.cursomc.services.EmailService;
import com.yan.cursomc.services.MockEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {
    @Autowired
    private DBService dbService;

    @Bean
    public boolean instantiateDatabase() throws ParseException {
       dbService.instantiateDatabase();
       return true;
   } 

   @Bean
   public EmailService emailService(){
       return new MockEmailService();
   }
}