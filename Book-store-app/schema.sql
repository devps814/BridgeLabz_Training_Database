CREATE TABLE books(
                      id SERIAL PRIMARY KEY,
                      title VARCHAR(100),
                      author VARCHAR(100),
                      price NUMERIC(10,2),
                      stock INT
);

CREATE TABLE users(
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100),
                      balance NUMERIC(10,2)
);

CREATE TABLE orders(
                       id SERIAL PRIMARY KEY,
                       user_id INT REFERENCES users(id),
                       book_id INT REFERENCES books(id),
                       quantity INT,
                       order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE price_audit(
                            id SERIAL PRIMARY KEY,
                            book_id INT,
                            old_price NUMERIC(10,2),
                            new_price NUMERIC(10,2),
                            changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_price_change()
RETURNS TRIGGER AS
$$
BEGIN

INSERT INTO price_audit(
    book_id,
    old_price,
    new_price
)
VALUES(
          OLD.id,
          OLD.price,
          NEW.price
      );

RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER price_change_trigger
    AFTER UPDATE OF price
    ON books
    FOR EACH ROW
    EXECUTE FUNCTION log_price_change();

INSERT INTO books(
    title,
    author,
    price,
    stock
)
VALUES
    ('Java Fundamentals','James Gosling',499,10),
    ('Spring Boot Guide','Craig Walls',699,5);

INSERT INTO users(
    name,
    balance
)
VALUES
    ('Dev',5000),
    ('Rahul',1000);