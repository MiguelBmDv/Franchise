CREATE TABLE IF NOT EXISTS franquicia( 
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(255),
    descripcion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS sucursal( 
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(255),
    franquicia_id VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS producto( 
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(255),
    stock INT,
    sucursal_id VARCHAR(255)
);

