-- Roles
CREATE TABLE IF NOT EXISTS rol (
                                   id   BIGSERIAL PRIMARY KEY,
                                   nombre VARCHAR(30) UNIQUE NOT NULL
    );

-- Usuarios
CREATE TABLE IF NOT EXISTS usuario (
                                       id        BIGSERIAL PRIMARY KEY,
                                       username  VARCHAR(80) UNIQUE NOT NULL,
    password  VARCHAR(200)       NOT NULL,
    enabled   BOOLEAN            NOT NULL DEFAULT TRUE
    );

-- Relación N:M
CREATE TABLE IF NOT EXISTS usuario_rol (
                                           usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    rol_id     BIGINT NOT NULL REFERENCES rol(id)     ON DELETE CASCADE,
    PRIMARY KEY (usuario_id, rol_id)
    );

-- Seeds
INSERT INTO rol (nombre) VALUES ('ADMIN') ON CONFLICT (nombre) DO NOTHING;
INSERT INTO rol (nombre) VALUES ('USER')  ON CONFLICT (nombre) DO NOTHING;

-- Contraseñas BCrypt (admin123 / user123)
-- Si quieres otras, genera el hash y reemplaza.
INSERT INTO usuario (username, password, enabled)
VALUES ('admin', '$2a$10$F5sA6v3yH5q7Q0Kc8eU6rOQzN9Jg9VbqNqvQy8n0v1H5m2sZ4z3Uu', TRUE)
    ON CONFLICT (username) DO NOTHING;

INSERT INTO usuario (username, password, enabled)
VALUES ('user',  '$2a$10$S0x8mU0c2sQX1bV7y6nUtO9gT4vT0YwU6U8c2Z8c0cYpXG9s1eYv2', TRUE)
    ON CONFLICT (username) DO NOTHING;

-- Asignación de roles
INSERT INTO usuario_rol(usuario_id, rol_id)
SELECT u.id, r.id FROM usuario u JOIN rol r ON r.nombre='ADMIN' WHERE u.username='admin'
    ON CONFLICT DO NOTHING;

INSERT INTO usuario_rol(usuario_id, rol_id)
SELECT u.id, r.id FROM usuario u JOIN rol r ON r.nombre='USER' WHERE u.username='user'
    ON CONFLICT DO NOTHING;
