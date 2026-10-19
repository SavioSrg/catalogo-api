-- BÁSICO
-- 1. Produtos ativos com o nome da categoria, ordenados por nome.
select
    p.nome,
    p.sku as codigo_produto,
    p.descricao,
    p.preco,
    p.criado_em,
    c.nome as categoria
from produto p
inner join categoria c on p.categoria_id = c.id
where p.ativo = true
order by p.nome asc;


-- 2. Produtos ativos de uma categoria, filtrando pelo nome da categoria.
select
    p.nome,
    p.sku as codigo_produto,
    p.descricao,
    p.preco,
    p.criado_em,
    c.nome as categoria
from produto p
inner join categoria c on p.categoria_id = c.id
where p.ativo = true
  and c.nome = 'Papelaria';


-- 3. Busca por parte do nome, sem diferenciar maiúscula de minúscula (ILIKE).
select
    p.nome,
    p.sku as codigo_produto,
    p.descricao,
    p.preco,
    p.criado_em,
    c.nome as categoria
from produto p
inner join categoria c on p.categoria_id = c.id
where p.ativo = true
  and p.nome ilike '%caderno%';


-- 4. Produtos com preço entre dois valores, do mais caro ao mais barato, paginados.
select
    p.nome,
    p.sku as codigo_produto,
    p.descricao,
    p.preco,
    p.criado_em,
    c.nome as categoria
from produto p
inner join categoria c on p.categoria_id = c.id
where p.ativo = true
  and p.preco between 15 and 200
order by p.preco desc
limit 3
offset 0;

-- INTERMEDIÁRIO
-- 5. Quantidade de produtos por categoria, incluindo a categoria sem produtos.
select
    c.nome as categoria,
    count(p.id) as total_produtos
from categoria c
left join produto p on p.categoria_id = c.id
group by c.id
order by total_produtos desc;


-- 6. Estoque atual de cada produto (entradas menos saídas), 0 para quem não tem movimentação.
select
    p.nome,
    coalesce(
        sum(
            case
                when me.tipo = 'ENTRADA' then me.quantidade
                when me.tipo = 'SAIDA'   then -me.quantidade
                else 0
            end
        ),
        0
    ) as estoque_atual
from produto p
left join movimentacao_estoque me on me.produto_id = p.id
group by p.id;


-- 7. Produtos ativos com estoque abaixo de 10.
select
    p.nome,
    coalesce(
        sum(
            case
                when me.tipo = 'ENTRADA' then me.quantidade
                when me.tipo = 'SAIDA'   then -me.quantidade
                else 0
            end
        ),
        0
    ) as estoque_atual
from produto p
left join movimentacao_estoque me on me.produto_id = p.id
where p.ativo = true
group by p.id
having coalesce(
        sum(
            case
                when me.tipo = 'ENTRADA' then me.quantidade
                when me.tipo = 'SAIDA'   then -me.quantidade
                else 0
            end
        ),
        0
    ) < 10;


-- 8. Produtos que nunca tiveram movimentação.
select p.nome
from produto p
left join movimentacao_estoque me on me.produto_id = p.id
where me.id is null;


-- 9. Total de saídas por mês.
select
    date_trunc('month', me.data_movimentacao) as mes,
    sum(me.quantidade) as total
from movimentacao_estoque me
where me.tipo = 'SAIDA'
group by date_trunc('month', me.data_movimentacao)
order by mes;


-- 10. Valor total em estoque (preço x estoque) por categoria.
-- A CTE calcula o estoque de cada produto antes de agrupar por categoria.
with estoque_calculado as (
    select
        p.id as produto_id,
        p.categoria_id,
        p.preco,
        sum(
            case
                when me.tipo = 'ENTRADA' then me.quantidade
                when me.tipo = 'SAIDA'   then -me.quantidade
                else 0
            end
        ) as qtd_estoque
    from produto p
    left join movimentacao_estoque me on me.produto_id = p.id
    group by p.id, p.categoria_id, p.preco
)
select
    c.nome as categoria,
    coalesce(sum(ec.qtd_estoque * ec.preco), 0) as valor_total
from categoria c
left join estoque_calculado ec on ec.categoria_id = c.id
group by c.id, c.nome;


-- MUITOS PARA MUITOS

-- 11a. Fornecedores de um produto (dois JOIN passando pela tabela do meio).
select
    p.nome as produto_nome,
    f.nome as fornecedor_nome
from produto p
inner join produto_fornecedor pf on pf.produto_id = p.id
inner join fornecedor f on f.id = pf.fornecedor_id
where p.nome ilike '%tecl%';

-- 11b. Produtos de um fornecedor.
select
    f.nome as fornecedor_nome,
    p.nome as produto_nome
from fornecedor f
inner join produto_fornecedor pf on pf.fornecedor_id = f.id
inner join produto p on p.id = pf.produto_id
where f.nome ilike '%alfa%';


-- 12. Fornecedores sem nenhum produto e produtos sem nenhum fornecedor.
select
    p.nome as item_sem_vinculo,
    'Produto sem fornecedor' as tipo
from produto p
left join produto_fornecedor pf on pf.produto_id = p.id
where pf.produto_id is null
union all
select
    f.nome as item_sem_vinculo,
    'Fornecedor sem produto' as tipo
from fornecedor f
left join produto_fornecedor pf on pf.fornecedor_id = f.id
where pf.fornecedor_id is null;


-- 13. Produtos que têm mais de um fornecedor.
select
    p.nome as nome_produto,
    count(pf.fornecedor_id) as total_fornecedores
from produto p
inner join produto_fornecedor pf on pf.produto_id = p.id
group by p.id, p.nome
having count(pf.fornecedor_id) > 1;


-- 14. Os 3 produtos com maior total de saídas.
select
    p.nome as produto_nome,
    sum(me.quantidade) as total_saidas
from produto p
inner join movimentacao_estoque me on me.produto_id = p.id
where me.tipo = 'SAIDA'
group by p.id, p.nome
order by total_saidas desc
limit 3;