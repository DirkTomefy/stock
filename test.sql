

CREATE TABLE categories (
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(255)
);

CREATE TABLE products (
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	price DOUBLE PRECISION NOT NULL,
	stock_quantity INTEGER NOT NULL,
	category_id INTEGER REFERENCES categories(id)
);

INSERT INTO categories (name, description) VALUES
('Boissons', 'Produits liquides à boire'),
('Snacks', 'Petits encas salés ou sucrés');

INSERT INTO products (name, price, stock_quantity, category_id) VALUES
('Eau minérale', 0.85, 120, 1),
('Jus d''orange', 1.95, 48, 1),
('Chips nature', 1.50, 75, 2),
('Barre chocolatée', 1.20, 64, 2);


