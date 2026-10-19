create table fornecedor (
id BIGINT GENERATED ALWAYS AS identity PRIMARY KEY,
nome varchar(150) not null,
cnpj VARCHAR(14) unique,
email varchar(150) not null
);

create table produto_fornecedor (
produto_id BIGINT NOT NULL REFERENCES produto(id) ON DELETE CASCADE,
fornecedor_id BIGINT NOT NULL REFERENCES fornecedor(id) ON DELETE CASCADE,
PRIMARY KEY (produto_id, fornecedor_id)
);