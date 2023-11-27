package com.example.auto.config;

import com.example.auto.dtos.*;
import com.example.auto.models.Brand;
import com.example.auto.models.Model;
import com.example.auto.models.Offer;
import com.example.auto.models.User;
import com.example.auto.models.base.ExtendedBaseEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }
}
