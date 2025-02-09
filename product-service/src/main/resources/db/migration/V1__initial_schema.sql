CREATE TABLE IF NOT EXISTS brands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS product_db.products
(
    active      BIT            NOT NULL DEFAULT 1,
    price       DECIMAL(10, 2) NOT NULL,
    stock       INT            NOT NULL,
    brand_id    BIGINT         NULL,
    category_id BIGINT         NULL,
    id          BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    code        VARCHAR(50)    NOT NULL,
    description TEXT           NULL,
    name        VARCHAR(255)   NOT NULL,
    CONSTRAINT UK57ivhy5aj3qfmdvl6vxdfjs4p
        UNIQUE (code),
    CONSTRAINT FKa3a4mpsfdf4d2y6r8ra3sc8mv
        FOREIGN KEY (brand_id) REFERENCES product_db.brands (id),
    CONSTRAINT FKog2rp4qthbtt2lfyhfo32lsw9
        FOREIGN KEY (category_id) REFERENCES product_db.categories (id)
);

INSERT INTO brands (name) VALUES
    ('Nike'),
    ('Adidas'),
    ('Puma');

INSERT INTO categories (name) VALUES
    ('Sportswear'),
    ('Footwear'),
    ('Athletics');
