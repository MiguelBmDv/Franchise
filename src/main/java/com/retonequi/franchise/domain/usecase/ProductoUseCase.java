package com.retonequi.franchise.domain.usecase;

import java.util.UUID;

import com.retonequi.franchise.domain.api.IProductoServicePort;
import com.retonequi.franchise.domain.constants.Constants;
import com.retonequi.franchise.domain.enums.Messages;
import com.retonequi.franchise.domain.exceptions.DomainException;
import com.retonequi.franchise.domain.model.Producto;
import com.retonequi.franchise.domain.spi.IProductoPersistencePort;
import com.retonequi.franchise.domain.spi.ISucursalPersistencePort;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductoUseCase implements IProductoServicePort{

    private final IProductoPersistencePort productoPersistencePort;
    private final ISucursalPersistencePort sucursalPersistencePort;

    public ProductoUseCase(IProductoPersistencePort productoPersistencePort, ISucursalPersistencePort sucursalPersistencePort) {
        this.productoPersistencePort = productoPersistencePort;
        this.sucursalPersistencePort = sucursalPersistencePort;  
    }

    @Override
    public Mono<Producto> saveProducto(Producto producto) {
         if (producto.sucursalId() == null || producto.sucursalId().isBlank()) {
            return Mono.error(new DomainException(Messages.ID_EMPTY));
        }
        return sucursalPersistencePort.existsById(UUID.fromString(producto.sucursalId()))
            .flatMap(exists -> {
                if (Boolean.FALSE.equals(exists)) {
                    return Mono.error(new DomainException(Messages.SUCURSAL_NOT_FOUND));
                }
                return productoPersistencePort.existsByNombreAndSucursalId(
                    producto.nombre(), producto.sucursalId()
                );
            })
            .flatMap(exists -> {
                if (Boolean.TRUE.equals(exists)) {
                    return Mono.error(new DomainException(Messages.PRODUCTO_ALREADY_EXISTS));
                }
                return productoPersistencePort.saveProducto(producto);
            });
    }

    @Override
    public Mono<Void> updateProducto(Producto producto) {
        return productoPersistencePort.getProducto(producto.id())
            .switchIfEmpty(Mono.error(new DomainException(Messages.PRODUCTO_NOT_FOUND)))
            .flatMap(existing -> {
                if (!existing.sucursalId().equals(producto.sucursalId())) {
                    return Mono.error(new DomainException(Messages.FORBIDDEN_UPDATE_SUCURSAL_ID));
                }

                String nuevoNombre = (producto.nombre() == null || producto.nombre().isBlank())
                    ? existing.nombre()
                    : producto.nombre();

                Integer nuevoStock = (producto.stock() == null || producto.stock().equals(Constants.ZERO))
                    ? existing.stock()
                    : producto.stock();

                return productoPersistencePort.existsByNombreAndSucursalId(nuevoNombre, existing.sucursalId())
                    .flatMap(exists -> {
                        if (Boolean.TRUE.equals(exists) && !nuevoNombre.equals(existing.nombre())) {
                            return Mono.error(new DomainException(Messages.PRODUCTO_ALREADY_EXISTS));
                        }

                        Producto actualizado = new Producto(
                            existing.id(),
                            nuevoNombre,
                            nuevoStock,
                            existing.sucursalId(),
                            null
                        );
                        return productoPersistencePort.updateProducto(actualizado);
                    });
            });
    }


    @Override
    public Mono<Void> deleteProducto(UUID id) {
        if (id == null) {
            return Mono.error(new DomainException(Messages.ID_EMPTY));
        }

        return productoPersistencePort.getProducto(id)
            .switchIfEmpty(Mono.error(new DomainException(Messages.PRODUCTO_NOT_FOUND)))
            .flatMap(existing -> productoPersistencePort.deleteProducto(id));
    }

    @Override
    public Mono<Producto> getProducto(UUID id) {
        if (id == null) {
            return Mono.error(new DomainException(Messages.ID_EMPTY));
        }
        return productoPersistencePort.getProducto(id)
            .switchIfEmpty(Mono.error(new DomainException(Messages.PRODUCTO_NOT_FOUND)));
    }

    @Override
    public Flux<Producto> getAllProductos() {
        return productoPersistencePort.getAllProductos();
    }

    @Override
    public Flux<Producto> getTopStockBySucursal(UUID franquiciaId) {
        if (franquiciaId == null) {
            return Flux.error(new DomainException(Messages.ID_EMPTY));
        }

        return sucursalPersistencePort.findByFranquiciaId(franquiciaId.toString())
            .flatMap(sucursal ->
                productoPersistencePort.findTopBySucursalIdOrderByStockDesc(sucursal.id().toString())
                    .map(producto -> new Producto(
                        producto.id(),
                        producto.nombre(),
                        producto.stock(),
                        sucursal.id().toString(),
                        sucursal.nombre()
                    ))
            );
    }


}
