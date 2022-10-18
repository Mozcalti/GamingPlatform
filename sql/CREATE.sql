BEGIN;


CREATE TABLE IF NOT EXISTS public.perfiles
(
    id_perfil serial NOT NULL,
    perfil character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT "PERFILES_pkey" PRIMARY KEY (id_perfil)
);

CREATE TABLE IF NOT EXISTS public.usuarios
(
    id_usuario serial NOT NULL,
    nombre character varying(100),
    apellidos character varying(150),
    correo character varying(150),
    id_perfil integer,
    id_institucion integer,
    PRIMARY KEY (id_usuario)
);

CREATE TABLE IF NOT EXISTS public.instituciones
(
    id_institucion serial NOT NULL,
    nombre character varying(200),
    fecha_creacion date,
    correo character varying(150),
    logo bytea,
    PRIMARY KEY (id_institucion)
);

CREATE TABLE IF NOT EXISTS public.participantes
(
    id_participante serial NOT NULL,
    nombre character varying(100),
    apellidos character varying(150),
    correo character varying(150),
    carrera character varying(250),
    foto bytea,
    academia character varying(150),
    semestre integer,
    id_institucion integer,
    PRIMARY KEY (id_participante)
);

CREATE TABLE IF NOT EXISTS public.torneos
(
    id_torneo serial NOT NULL,
    fecha_inicio character varying(30),
    fecha_fin character varying(30),
    num_etapas integer,
    PRIMARY KEY (id_torneo)
);

CREATE TABLE IF NOT EXISTS public.etapas
(
    id_etapa serial NOT NULL,
    numero_etapa integer,
    fecha_inicio character varying(30),
    fecha_fin character varying(30),
    id_torneo integer,
    PRIMARY KEY (id_etapa)
);

CREATE TABLE IF NOT EXISTS public.equipos
(
    id_equipo serial NOT NULL,
    nombre character varying(200),
    PRIMARY KEY (id_equipo)
);

CREATE TABLE IF NOT EXISTS public.reglas
(
    id_regla serial NOT NULL,
    num_competidores integer,
    num_rondas integer,
    id_etapa integer,
    tiempo_batalla_aprox integer,
    trabajo character varying(30),
    tiempo_espera integer,
    PRIMARY KEY (id_regla)
);

CREATE TABLE IF NOT EXISTS public.participante_equipo
(
    id_participante_equipo serial NOT NULL,
    id_participante integer NOT NULL,
    id_equipo integer NOT NULL,
    PRIMARY KEY (id_participante_equipo)
);

CREATE TABLE IF NOT EXISTS public.etapa_equipo
(
    id_etapa_equipo serial NOT NULL,
    id_etapa integer NOT NULL,
    id_equipo integer NOT NULL,
    PRIMARY KEY (id_etapa_equipo)
);

CREATE TABLE IF NOT EXISTS public.torneo_horas_habiles
(
    id_hora_habil serial NOT NULL,
    hora_ini_habil character varying(30),
    hora_fin_habil character varying(30),
    id_torneo integer,
    PRIMARY KEY (id_hora_habil)
);

CREATE TABLE IF NOT EXISTS public.batallas
(
    id_batalla serial NOT NULL,
    fecha character varying(50),
    hora_inicio character varying(30),
    hora_fin character varying(30),
    rondas integer,
    bnd_envio_correo integer DEFAULT 0,
    PRIMARY KEY (id_batalla)
);

CREATE TABLE IF NOT EXISTS public.etapa_batalla
(
    id_etapa_batalla serial NOT NULL,
    id_etapa integer,
    id_batalla integer,
    PRIMARY KEY (id_etapa_batalla)
);

CREATE TABLE IF NOT EXISTS public.batalla_participantes
(
    id_batalla_participante serial NOT NULL,
    id_participante_equipo integer,
    nombre character varying,
    id_batalla integer,
    PRIMARY KEY (id_participante_equipo)
);

ALTER TABLE IF EXISTS public.usuarios
    ADD CONSTRAINT "FK_PERFILES" FOREIGN KEY (id_perfil)
    REFERENCES public.perfiles (id_perfil) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.usuarios
    ADD FOREIGN KEY (id_institucion)
    REFERENCES public.instituciones (id_institucion) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.participantes
    ADD FOREIGN KEY (id_institucion)
    REFERENCES public.instituciones (id_institucion) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.etapas
    ADD FOREIGN KEY (id_torneo)
    REFERENCES public.torneos (id_torneo) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.reglas
    ADD FOREIGN KEY (id_etapa)
    REFERENCES public.etapas (id_etapa) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.participante_equipo
    ADD FOREIGN KEY (id_participante)
    REFERENCES public.participantes (id_participante) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.participante_equipo
    ADD FOREIGN KEY (id_equipo)
    REFERENCES public.equipos (id_equipo) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.etapa_equipo
    ADD FOREIGN KEY (id_etapa)
    REFERENCES public.etapas (id_etapa) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.etapa_equipo
    ADD FOREIGN KEY (id_equipo)
    REFERENCES public.equipos (id_equipo) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.torneo_horas_habiles
    ADD FOREIGN KEY (id_torneo)
    REFERENCES public.torneos (id_torneo) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.etapa_batalla
    ADD FOREIGN KEY (id_etapa)
    REFERENCES public.etapas (id_etapa) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.etapa_batalla
    ADD FOREIGN KEY (id_batalla)
    REFERENCES public.batallas (id_batalla) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.batalla_participantes
    ADD FOREIGN KEY (id_batalla)
    REFERENCES public.batallas (id_batalla) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

END;