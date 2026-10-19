INSERT INTO categoria (nome) VALUES
('Eletrônicos'), ('Periféricos'), ('Papelaria'), ('Móveis');

INSERT INTO fornecedor (nome, cnpj, email) VALUES
('TechSul Distribuidora', '12345678000190', 'contato@techsul.example.com'),
('Global Periféricos', '23456789000101', 'vendas@globalperifericos.example.com'),
('Papel & Cia', '34567890000112', 'comercial@papelecia.example.com'),
('Importadora Alfa', '45678901000123', 'alfa@importadoraalfa.example.com');

INSERT INTO produto (nome, sku, descricao, preco, ativo, categoria_id) VALUES
('Notebook 14" Core i5', 'ELE-001', '8 GB de RAM, SSD 256 GB', 3899.90, TRUE, 1),
('Smartphone 128 GB', 'ELE-002', NULL, 1999.00, TRUE, 1),
('Tablet 10 polegadas', 'ELE-003', 'Wi-Fi, 64 GB', 1299.90, TRUE, 1),
('Fone Bluetooth', 'ELE-004', NULL, 249.90, TRUE, 1),
('Mouse sem fio', 'PER-001', 'Receptor USB', 89.90, TRUE, 2),
('Teclado mecânico', 'PER-002', 'Layout ABNT2', 329.90, TRUE, 2),
('Monitor 24" Full HD', 'PER-003', NULL, 799.00, TRUE, 2),
('Webcam HD', 'PER-004', '720p com microfone', 159.90, TRUE, 2),
('Caderno 200 folhas', 'PAP-001', NULL, 24.90, TRUE, 3),
('Caneta esferográfica (caixa com 50)', 'PAP-002', NULL, 39.90, TRUE, 3),
('Agenda 2027', 'PAP-003', 'Capa dura', 49.90, TRUE, 3),
('Grampeador de mesa', 'PAP-004', NULL, 29.90, FALSE, 3);

INSERT INTO movimentacao_estoque (produto_id, tipo, quantidade, observacao, data_movimentacao) VALUES
(1, 'ENTRADA', 20, 'Compra inicial', '2026-08-03 09:00'),
(1, 'SAIDA', 6, 'Venda', '2026-08-20 14:30'),
(1, 'SAIDA', 5, 'Venda', '2026-09-12 11:00'),
(1, 'SAIDA', 4, 'Venda', '2026-10-05 16:45'),
(2, 'ENTRADA', 50, 'Compra inicial', '2026-08-03 09:00'),
(2, 'SAIDA', 18, 'Venda', '2026-08-25 10:20'),
(2, 'SAIDA', 22, 'Venda', '2026-09-15 15:10'),
(2, 'SAIDA', 8, 'Venda', '2026-10-08 13:00'),
(3, 'ENTRADA', 15, 'Compra inicial', '2026-08-10 09:30'),
(3, 'SAIDA', 3, 'Venda', '2026-09-02 17:00'),
(3, 'SAIDA', 2, 'Venda', '2026-10-01 12:15'),
(4, 'ENTRADA', 100, 'Compra inicial', '2026-08-03 09:00'),
(4, 'SAIDA', 40, 'Venda', '2026-08-30 18:00'),
(4, 'SAIDA', 35, 'Venda', '2026-09-20 10:40'),
(5, 'ENTRADA', 200, 'Compra inicial', '2026-08-05 08:30'),
(5, 'SAIDA', 80, 'Venda', '2026-09-01 14:00'),
(5, 'SAIDA', 70, 'Venda', '2026-09-22 16:20'),
(6, 'ENTRADA', 40, 'Compra inicial', '2026-08-05 08:30'),
(6, 'SAIDA', 15, 'Venda', '2026-09-10 11:50'),
(7, 'ENTRADA', 30, 'Compra inicial', '2026-08-12 10:00'),
(7, 'SAIDA', 10, 'Venda', '2026-09-05 15:30'),
(7, 'SAIDA', 8, 'Venda', '2026-10-09 09:45'),
(9, 'ENTRADA', 300, 'Compra inicial', '2026-08-02 08:00'),
(9, 'SAIDA', 120, 'Venda', '2026-08-28 13:20'),
(9, 'SAIDA', 90, 'Venda', '2026-09-18 12:00'),
(10, 'ENTRADA', 150, 'Compra inicial', '2026-08-02 08:00'),
(10, 'SAIDA', 50, 'Venda', '2026-09-08 10:10'),
(10, 'SAIDA', 40, 'Venda', '2026-10-04 14:25'),
(11, 'ENTRADA', 80, 'Compra inicial', '2026-09-15 09:00'),
(11, 'SAIDA', 25, 'Venda', '2026-10-06 11:30'),
(12, 'ENTRADA', 25, 'Compra inicial', '2026-08-02 08:00'),
(12, 'SAIDA', 25, 'Descontinuado', '2026-09-30 17:00');

INSERT INTO produto_fornecedor (produto_id, fornecedor_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (4, 2),
(5, 2), (6, 2), (7, 2), (7, 1), (8, 2),
(9, 3), (10, 3), (12, 3);