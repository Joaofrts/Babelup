package com.example.babelup.configuration;

import com.example.babelup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Autowired
    private UsuarioService service;
    @Bean
    public boolean startDb(){
        service.startDb();
        return true;
    }
}
