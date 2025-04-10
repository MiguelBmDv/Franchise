package com.retonequi.franchise.applicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.retonequi.franchise.application.UseCasesConfig;
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

@ExtendWith(MockitoExtension.class)
class UseCasesConfigTest {

    @Mock
    private IFranquiciaRepository franquiciaRepository;
    @Mock
    private IFranquiciaEntityMapper franquiciaEntityMapper;

    @Mock
    private ISucursalRepository sucursalRepository;
    @Mock
    private ISucursalEntityMapper sucursalEntityMapper;

    @Mock
    private IProductoRepository productoRepository;
    @Mock
    private IProductoEntityMapper productoEntityMapper;

    private UseCasesConfig useCasesConfig;

    @BeforeEach
    void setUp() {
        useCasesConfig = new UseCasesConfig(
                franquiciaRepository,
                franquiciaEntityMapper,
                sucursalRepository,
                sucursalEntityMapper,
                productoRepository,
                productoEntityMapper
        );
    }

    @Test
    void shouldCreateFranquiciaUseCase() {
        IFranquiciaPersistencePort persistencePort = useCasesConfig.franquiciaPersistencePort();
        IFranquiciaServicePort servicePort = useCasesConfig.franquiciaServicePort(persistencePort);

        assertNotNull(persistencePort);
        assertNotNull(servicePort);
        assertTrue(persistencePort instanceof FranquiciaReactiveAdapter);
        assertTrue(servicePort instanceof FranquiciaUseCase);
    }

    @Test
    void shouldCreateSucursalUseCase() {
        ISucursalPersistencePort persistencePort = useCasesConfig.sucursalPersistencePort();
        IFranquiciaPersistencePort franquiciaPersistencePort = useCasesConfig.franquiciaPersistencePort();
        ISucursalServicePort servicePort = useCasesConfig.sucursalServicePort(persistencePort, franquiciaPersistencePort);

        assertNotNull(persistencePort);
        assertNotNull(servicePort);
        assertTrue(persistencePort instanceof SucursalReactiveAdapter);
        assertTrue(servicePort instanceof SucursalUseCase);
    }

    @Test
    void shouldCreateProductoUseCase() {
        IProductoPersistencePort persistencePort = useCasesConfig.productoPersistencePort();
        ISucursalPersistencePort sucursalPersistencePort = useCasesConfig.sucursalPersistencePort();
        IProductoServicePort servicePort = useCasesConfig.productoServicePort(persistencePort, sucursalPersistencePort);

        assertNotNull(persistencePort);
        assertNotNull(servicePort);
        assertTrue(persistencePort instanceof ProductoReactiveAdapter);
        assertTrue(servicePort instanceof ProductoUseCase);
    }
}
