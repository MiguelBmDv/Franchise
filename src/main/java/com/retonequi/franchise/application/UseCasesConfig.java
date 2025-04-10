package com.retonequi.franchise.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.retonequi.franchise.domain.api.IFranquiciaServicePort;
import com.retonequi.franchise.domain.api.ISucursalServicePort;
import com.retonequi.franchise.domain.spi.IFranquiciaPersistencePort;
import com.retonequi.franchise.domain.spi.ISucursalPersistencePort;
import com.retonequi.franchise.domain.usecase.FranquiciaUseCase;
import com.retonequi.franchise.domain.usecase.SucursalUseCase;
import com.retonequi.franchise.infraestructure.output.reactive.adapter.FranquiciaReactiveAdapter;
import com.retonequi.franchise.infraestructure.output.reactive.adapter.SucursalReactiveAdapter;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.IFranquiciaEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.ISucursalEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.repository.IFranquiciaRepository;
import com.retonequi.franchise.infraestructure.output.reactive.repository.ISucursalRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

    private final IFranquiciaRepository franquiciaRepository;
    private final IFranquiciaEntityMapper franquiciaEntityMapper;
    private final ISucursalRepository sucursalRepository;
    private final ISucursalEntityMapper sucursalEntityMapper;

    @Bean
    public IFranquiciaPersistencePort franquiciaPersistencePort(){
        return new FranquiciaReactiveAdapter(franquiciaRepository, franquiciaEntityMapper);
    }

    @Bean
    public IFranquiciaServicePort franquiciaServicePort(IFranquiciaPersistencePort franquiciaPersistencePort){
        return new FranquiciaUseCase(franquiciaPersistencePort);
    }

    @Bean
    public ISucursalPersistencePort sucursalPersistencePort(){
        return new SucursalReactiveAdapter(sucursalRepository, sucursalEntityMapper);
    }

    @Bean
    public ISucursalServicePort sucursalServicePort(ISucursalPersistencePort sucursalPersistencePort, IFranquiciaPersistencePort franquiciaPersistencePort){
        return new SucursalUseCase(sucursalPersistencePort,franquiciaPersistencePort);
    }


}
