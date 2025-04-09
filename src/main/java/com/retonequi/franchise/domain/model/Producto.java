package com.retonequi.franchise.domain.model;

import java.util.UUID;

public record Producto(UUID id, String nombre,Integer stock, UUID sucursalId){}
   