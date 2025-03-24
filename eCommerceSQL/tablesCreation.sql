CREATE TABLE users(
	user_id serial PRIMARY KEY,
	first_name varchar(30) NOT null,
	last_name varchar(30) NOT NULL,
	email varchar(30) UNIQUE NOT NULL,
	password varchar(30) NOT NULL,
	role varchar(30) DEFAULT 'USER'
);

CREATE TABLE orders(
	order_id serial PRIMARY KEY,
	total_price decimal NOT null,
	status varchar(30) DEFAULT 'PENDING',
	created_at timestamp DEFAULT NOW(),
	user_id int,
	FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE products(
	product_id serial PRIMARY KEY,
	name varchar(50) NOT NULL,
	description TEXT,
	price decimal NOT NULL,
	stock int DEFAULT 1
);

CREATE TABLE cart_items(
	cart_item_id serial PRIMARY KEY,
	quantity int NOT NULL,
	user_id int,
	product_id int,
	FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
	FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE TABLE order_items(
	order_item_id serial PRIMARY KEY,
	quantity int NOT NULL,
	price decimal NOT NULL,
	order_id int,
	product_id int,
	FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
	FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

