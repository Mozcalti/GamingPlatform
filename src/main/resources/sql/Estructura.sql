create table cat_institucion
(
    id             uuid DEFAULT gen_random_uuid() primary key,
    nombre         varchar(255) not null,
    correo         varchar(255) null,
    fecha_creacion varchar(20)  not null
);

    CREATE TABLE usuarios
    (
        id         SERIAL PRIMARY KEY,
        email      VARCHAR(50)  NOT NULL,
        password   VARCHAR(100) NOT NULL,
        habilitado BOOL DEFAULT false,
        rol        VARCHAR(10)  NOT NULL
    );
