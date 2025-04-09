package com.retonequi.franchise.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.retonequi.franchise.domain.api.IFranquiciaServicePort;
import com.retonequi.franchise.domain.spi.IFranquiciaPersistencePort;
import com.retonequi.franchise.domain.usecase.FranquiciaUseCase;
import com.retonequi.franchise.infraestructure.output.jpa.adapter.FranquiciaJpaAdapter;
import com.retonequi.franchise.infraestructure.output.jpa.mapper.IFranquiciaEntityMapper;
import com.retonequi.franchise.infraestructure.output.jpa.repository.IFranquiciaRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

    private final IFranquiciaRepository franquiciaRepository;
    private final IFranquiciaEntityMapper franquiciaEntityMapper;

    @Bean
    public IFranquiciaPersistencePort franquiciaPersistencePort(){
        return new FranquiciaJpaAdapter(franquiciaRepository, franquiciaEntityMapper);
    }

    @Bean
    public IFranquiciaServicePort franquiciaServicePort(IFranquiciaPersistencePort franquiciaPersistencePort){
        return new FranquiciaUseCase(franquiciaPersistencePort);
    }

}
