ALTER TABLE IF EXISTS batallas
    DROP COLUMN IF EXISTS bnd_termina;

ALTER TABLE IF EXISTS batallas
    ADD COLUMN estatus character varying(30);

ALTER TABLE IF EXISTS reglas
    ADD COLUMN arena_ancho int,
    ADD COLUMN arena_alto int;