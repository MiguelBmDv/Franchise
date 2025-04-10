package com.retonequi.franchise.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.retonequi.franchise.domain.api.IFranquiciaServicePort;
import com.retonequi.franchise.domain.api.IProductoServicePort;
import com.retonequi.franchise.domain.api.ISucursalServicePort;
import com.retonequi.franchise.domain.spi.IFranquiciaPersistencePort;
import com.retonequi.franchise.domain.spi.IProductoPersistencePort;
import com.retonequi.franchise.domain.spi.ISucursalPersistencePort;
import com.retonequi.franchise.domain.usecase.FranquiciaUseCase;
import com.retonequi.franchise.domain.usecase.ProductoUseCase;
import com.retonequi.franchise.domain.usecase.SucursalUseCase;
import com.retonequi.franchise.infraestructure.output.reactive.adapter.FranquiciaReactiveAdapter;
import com.retonequi.franchise.infraestructure.output.reactive.adapter.ProductoReactiveAdapter;
import com.retonequi.franchise.infraestructure.output.reactive.adapter.SucursalReactiveAdapter;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.IFranquiciaEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.IProductoEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.mapper.ISucursalEntityMapper;
import com.retonequi.franchise.infraestructure.output.reactive.repository.IFranquiciaRepository;
import com.retonequi.franchise.infraestructure.output.reactive.repository.IProductoRepository;
import com.retonequi.franchise.infraestructure.output.reactive.repository.ISucursalRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

    private final IFranquiciaRepository franquiciaRepository;
    private final IFranquiciaEntityMapper franquiciaEntityMapper;
    private final ISucursalRepository sucursalRepository;
    private final ISucursalEntityMapper sucursalEntityMapper;
    private final IProductoRepository productoRepository;
    private final IProductoEntityMapper productoEntityMapper;

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

    
    @Bean
    public IProductoPersistencePort productoPersistencePort(){
        return new ProductoReactiveAdapter(productoRepository, productoEntityMapper);
    }

    @Bean
    public IProductoServicePort productoServicePort( IProductoPersistencePort productoPersistencePort,ISucursalPersistencePort sucursalPersistencePort){
        return new ProductoUseCase(productoPersistencePort,sucursalPersistencePort);
    }


}
