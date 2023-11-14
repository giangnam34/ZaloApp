package com.essay.zaloapp;

import com.essay.zaloapp.config.AppProperties;
import com.essay.zaloapp.config.FileStorageProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class, AppProperties.class})
public class ZaloApp {


    public static void main(String[] args) {
        SpringApplication.run(ZaloApp.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
