create table cat_institucion(
    id uuid DEFAULT gen_random_uuid() primary key,
    nombre varchar(255) not null,
    correo varchar(255) null,
    fecha_creacion varchar(20) not null,
    logo varchar not null
);


drop table cat_institucion;
