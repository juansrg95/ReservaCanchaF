
-- V1__init.sql
CREATE TABLE sede (
                      id BIGSERIAL PRIMARY KEY,
                      nombre    VARCHAR(100) NOT NULL,
                      direccion VARCHAR(200) NOT NULL
);

CREATE TABLE cancha (
                        id BIGSERIAL PRIMARY KEY,
                        sede_id BIGINT NOT NULL REFERENCES sede(id),
                        nombre   VARCHAR(100) NOT NULL,
                        deporte  VARCHAR(50)  NOT NULL,
                        activa   BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE reserva (
                         id BIGSERIAL PRIMARY KEY,
                         cancha_id BIGINT NOT NULL REFERENCES cancha(id),
                         usuario   VARCHAR(120) NOT NULL,
                         inicio    TIMESTAMP NOT NULL,
                         fin       TIMESTAMP NOT NULL,
                         estado    VARCHAR(20) NOT NULL DEFAULT 'ACTIVA'
);



CREATE INDEX idx_reserva_cancha_fecha ON reserva(cancha_id, inicio, fin);
