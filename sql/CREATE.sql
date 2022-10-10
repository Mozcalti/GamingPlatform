BEGIN;


CREATE TABLE IF NOT EXISTS perfiles
(
    id_perfil serial NOT NULL,
    perfil character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT "PERFILES_pkey" PRIMARY KEY (id_perfil)
);

CREATE TABLE IF NOT EXISTS usuarios
(
    id_usuario serial NOT NULL,
    nombre character varying(100),
    apellidos character varying(150),
    correo character varying(150),
    id_perfil integer,
    id_institucion integer,
    PRIMARY KEY (id_usuario)
);

CREATE TABLE IF NOT EXISTS instituciones
(
    id_institucion serial NOT NULL,
    nombre character varying(200),
    fecha_creacion date,
    correo character varying(150),
    logo bytea,
    PRIMARY KEY (id_institucion)
);

CREATE TABLE IF NOT EXISTS participantes
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

CREATE TABLE IF NOT EXISTS torneos
(
    id_torneo serial NOT NULL,
    fecha_inicio character varying(30),
    fecha_fin character varying(30),
    num_etapas integer,
    PRIMARY KEY (id_torneo)
);

CREATE TABLE IF NOT EXISTS etapas
(
    id_etapa serial NOT NULL,
    numero_etapa integer,
    fecha_inicio character varying(30),
    fecha_fin character varying(30),
    id_torneo integer,
    PRIMARY KEY (id_etapa)
);

CREATE TABLE IF NOT EXISTS equipos
(
    id_equipo serial NOT NULL,
    nombre character varying(200),
    PRIMARY KEY (id_equipo)
);

CREATE TABLE IF NOT EXISTS reglas
(
    id_regla serial NOT NULL,
    num_competidores integer,
    num_rondas integer,
    id_etapa integer,
    PRIMARY KEY (id_regla)
);

CREATE TABLE IF NOT EXISTS participante_equipo
(
    id_participante_equipo serial NOT NULL,
    id_participante integer NOT NULL,
    id_equipo integer NOT NULL,
    PRIMARY KEY (id_participante_equipo)
);

CREATE TABLE IF NOT EXISTS etapa_equipo
(
    id_etapa_equipo serial NOT NULL,
    id_etapa integer NOT NULL,
    id_equipo integer NOT NULL,
    PRIMARY KEY (id_etapa_equipo)
);

ALTER TABLE IF EXISTS usuarios
    ADD CONSTRAINT "FK_PERFILES" FOREIGN KEY (id_perfil)
    REFERENCES perfiles (id_perfil) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS usuarios
    ADD FOREIGN KEY (id_institucion)
    REFERENCES instituciones (id_institucion) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS participantes
    ADD FOREIGN KEY (id_institucion)
    REFERENCES instituciones (id_institucion) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS etapas
    ADD FOREIGN KEY (id_torneo)
    REFERENCES torneos (id_torneo) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS reglas
    ADD FOREIGN KEY (id_etapa)
    REFERENCES etapas (id_etapa) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS participante_equipo
    ADD FOREIGN KEY (id_participante)
    REFERENCES participantes (id_participante) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS participante_equipo
    ADD FOREIGN KEY (id_equipo)
    REFERENCES equipos (id_equipo) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS etapa_equipo
    ADD FOREIGN KEY (id_etapa)
    REFERENCES etapas (id_etapa) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS etapa_equipo
    ADD FOREIGN KEY (id_equipo)
    REFERENCES equipos (id_equipo) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

END;