create table categoria (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY key,
nome varchar(100) not null unique
);

create table produto (
id BIGINT GENERATED ALWAYS AS identity PRIMARY KEY,
nome varchar(150) not null,
sku VARCHAR(50) NOT NULL UNIQUE,
descricao varchar(180),
preco NUMERIC(10,2) not null CHECK (preco >= 0),
ativo boolean NOT NULL DEFAULT TRUE,
categoria_id BIGINT NOT NULL REFERENCES categoria(id) ON DELETE RESTRICT,
criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
