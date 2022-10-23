CREATE TABLE verification_token
(
    id              serial                NOT NULL,
    token           character varying(37) NOT NULL,
    expiry_date     timestamp             NOT NULL,
    activation_date timestamp,
    user_id         integer               NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE verification_token
    ADD FOREIGN KEY (user_id)
        REFERENCES usuarios (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID;