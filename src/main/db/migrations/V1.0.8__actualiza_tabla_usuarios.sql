ALTER TABLE usuarios
ALTER COLUMN rol type varchar(15) using rol::varchar(15);