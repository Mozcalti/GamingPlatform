ALTER TABLE participantes
    ALTER COLUMN fecha_creacion TYPE TIMESTAMP WITHOUT TIME ZONE USING fecha_creacion::TIMESTAMP,
    ALTER COLUMN fecha_creacion SET NOT NULL;


ALTER TABLE institucion
    ALTER COLUMN fecha_creacion TYPE TIMESTAMP WITHOUT TIME ZONE USING fecha_creacion::TIMESTAMP,
    ALTER COLUMN fecha_creacion SET NOT NULL;