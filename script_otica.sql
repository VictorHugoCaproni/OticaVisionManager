-- Script SQL para o contexto de Ótica / Loja de Óculos
USE crud_manager;

-- 1. Atualização na tabela users para suportar o Login
ALTER TABLE users ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT '123456';

-- 2. Tabela Armações (CRUD 1 - relacionada com Fornecedor/Fabricante em companies)
CREATE TABLE IF NOT EXISTS frames (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model_name VARCHAR(120) NOT NULL,
    material VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    manufacture_date DATE NOT NULL,
    company_id INT NOT NULL,
    FOREIGN KEY (company_id) REFERENCES companies(id)
);

-- 3. Tabela Pedidos de Óculos / Receitas (CRUD 2 - relacionada com Cliente/Vendedor em users e Armação em frames)
CREATE TABLE IF NOT EXISTS optical_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prescription_code VARCHAR(80) NOT NULL,
    lens_type VARCHAR(50) NOT NULL,
    total_value DECIMAL(10, 2) NOT NULL,
    delivery_date DATE NOT NULL,
    delivered BOOLEAN NOT NULL DEFAULT FALSE,
    observations TEXT,
    user_id INT NOT NULL,
    frame_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (frame_id) REFERENCES frames(id)
);
