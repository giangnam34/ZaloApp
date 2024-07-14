package com.essay.zaloapp;

import com.essay.zaloapp.config.AppProperties;
import com.essay.zaloapp.config.FileStorageProperties;
import com.essay.zaloapp.services.RecommendSystemService;
import com.essay.zaloapp.services.impl.RecommendSystemServiceImpl;
import org.apache.mahout.cf.taste.common.TasteException;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class, AppProperties.class})
public class ZaloApp {


    public static void main(String[] args) throws IOException, TasteException {
        SpringApplication.run(ZaloApp.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
