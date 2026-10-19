create table movimentacao_estoque (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY key,
produto_id BIGINT NOT NULL REFERENCES produto(id) ON DELETE RESTRICT,
tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),
quantidade INT NOT NULL CHECK (quantidade > 0),
observacao VARCHAR(180),
data_movimentacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);