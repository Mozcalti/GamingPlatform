create table institucion
(
    id             serial primary key,
    nombre         varchar(255) not null,
    correo         varchar(255) null,
    fecha_creacion varchar(20)  not null,
    logo           varchar      not null
);


CREATE TABLE usuarios
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(50)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    habilitado BOOL DEFAULT false,
    rol        VARCHAR(10)  NOT NULL
);

CREATE TABLE participantes
(
    id_participante serial                 NOT NULL,
    nombre          character varying(100) not null,
    apellidos       character varying(150) not null,
    correo          character varying(150) not null,
    academia        character varying(150) not null,
    ies             varchar(255)           not null,
    carrera         character varying(250) null,
    semestre        integer                null,
    foto            varchar                not null,
    fecha_creacion  varchar(20)            not null,
    id_institucion  integer,
    PRIMARY KEY (id_participante)
);

CREATE TABLE torneos
(
    id_torneo    serial NOT NULL,
    fecha_inicio character varying(30),
    fecha_fin    character varying(30),
    num_etapas   integer,
    PRIMARY KEY (id_torneo)
);

CREATE TABLE etapas
(
    id_etapa     serial NOT NULL,
    numero_etapa integer,
    fecha_inicio character varying(30),
    fecha_fin    character varying(30),
    id_torneo    integer,
    PRIMARY KEY (id_etapa)
);

CREATE TABLE equipos
(
    id_equipo serial NOT NULL,
    nombre    character varying(200),
    PRIMARY KEY (id_equipo)
);

CREATE TABLE reglas
(
    id_regla             serial NOT NULL,
    num_competidores     integer,
    num_rondas           integer,
    id_etapa             integer,
    tiempo_batalla_aprox integer,
    trabajo              character varying(30),
    tiempo_espera        integer,
    PRIMARY KEY (id_regla)
);

CREATE TABLE participante_equipo
(
    id_participante_equipo serial  NOT NULL,
    id_participante        integer NOT NULL,
    id_equipo              integer NOT NULL,
    PRIMARY KEY (id_participante_equipo)
);

CREATE TABLE etapa_equipo
(
    id_etapa_equipo serial  NOT NULL,
    id_etapa        integer NOT NULL,
    id_equipo       integer NOT NULL,
    PRIMARY KEY (id_etapa_equipo)
);

CREATE TABLE torneo_horas_habiles
(
    id_hora_habil  serial NOT NULL,
    hora_ini_habil character varying(30),
    hora_fin_habil character varying(30),
    id_torneo      integer,
    PRIMARY KEY (id_hora_habil)
);

CREATE TABLE batallas
(
    id_batalla       serial NOT NULL,
    fecha            character varying(50),
    hora_inicio      character varying(30),
    hora_fin         character varying(30),
    rondas           integer,
    bnd_envio_correo integer DEFAULT 0,
    PRIMARY KEY (id_batalla)
);

CREATE TABLE etapa_batalla
(
    id_etapa_batalla serial NOT NULL,
    id_etapa         integer,
    id_batalla       integer,
    PRIMARY KEY (id_etapa_batalla)
);

CREATE TABLE batalla_participantes
(
    id_batalla_participante serial NOT NULL,
    id_participante_equipo  integer,
    nombre                  character varying,
    id_batalla              integer,
    PRIMARY KEY (id_participante_equipo)
);

ALTER TABLE etapas
    ADD FOREIGN KEY (id_torneo)
        REFERENCES torneos (id_torneo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE reglas
    ADD FOREIGN KEY (id_etapa)
        REFERENCES etapas (id_etapa) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE participante_equipo
    ADD FOREIGN KEY (id_participante)
        REFERENCES participantes (id_participante) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE participante_equipo
    ADD FOREIGN KEY (id_equipo)
        REFERENCES equipos (id_equipo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE etapa_equipo
    ADD FOREIGN KEY (id_etapa)
        REFERENCES etapas (id_etapa) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE etapa_equipo
    ADD FOREIGN KEY (id_equipo)
        REFERENCES equipos (id_equipo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE torneo_horas_habiles
    ADD FOREIGN KEY (id_torneo)
        REFERENCES torneos (id_torneo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE etapa_batalla
    ADD FOREIGN KEY (id_etapa)
        REFERENCES etapas (id_etapa) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE etapa_batalla
    ADD FOREIGN KEY (id_batalla)
        REFERENCES batallas (id_batalla) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;


ALTER TABLE batalla_participantes
    ADD FOREIGN KEY (id_batalla)
        REFERENCES batallas (id_batalla) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;
