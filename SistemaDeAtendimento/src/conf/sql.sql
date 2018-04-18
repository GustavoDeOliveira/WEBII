/* DROP DATABASE elifoot; */
CREATE DATABASE elifoot WITH ENCODING = 'UTF-8' LC_COLLATE = 'pt_BR.UTF-8' LC_CTYPE = 'pt_BR.UTF-8';

CREATE TABLE public."time" (
	id SERIAL,
	nome varchar(255) NOT NULL,
	CONSTRAINT time_pk PRIMARY KEY (id)
);

CREATE TABLE public.jogador (
	id SERIAL,
	nome varchar(255) NOT NULL,
	time_id int NOT NULL,
	CONSTRAINT jogador_pk PRIMARY KEY (id),
	CONSTRAINT jogador_time_fk FOREIGN KEY (time_id) REFERENCES "time" (id)
		ON UPDATE CASCADE
		ON DELETE NO ACTION
);

SELECT * FROM "time";
SELECT * FROM jogador;

SELECT * FROM posicao;