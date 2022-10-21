ALTER TABLE batallas
    ADD COLUMN bnd_termina integer;

CREATE TABLE robots
(
    id_robot serial,
    nombre character varying(250),
    activo integer,
    id_equipo integer,
    PRIMARY KEY (id_robot)
);

ALTER TABLE robots
    ADD FOREIGN KEY (id_equipo)
        REFERENCES public.equipos (id_equipo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
    NOT VALID;

CREATE TABLE resultados
(
    id_resultado serial NOT NULL,
    teamLeaderName character varying(250),
    rank integer,
    score double precision,
    survival double precision,
    lastSurvivorBonus double precision,
    bulletDamage double precision,
    bulletDamageBonus double precision,
    ramDamage double precision,
    ramDamageBonus double precision,
    firsts integer,
    seconds integer,
    thirds integer,
    ver integer,
    id_batalla integer,
    PRIMARY KEY (id_resultado)
);

ALTER TABLE resultados
    ADD FOREIGN KEY (id_batalla)
        REFERENCES public.batallas (id_batalla) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
    NOT VALID;
