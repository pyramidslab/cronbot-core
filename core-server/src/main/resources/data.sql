DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id VARCHAR(250),
  username VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  roles VARCHAR(250) DEFAULT NULL
);

INSERT INTO users (id, username, password) VALUES
  ('Aliko', 'Dangote', 'Billionaire Industrialist');