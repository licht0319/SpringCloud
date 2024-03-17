CREATE TABLE user_details (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL
);


CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    description TEXT,
    user_id INTEGER REFERENCES user_details(id)
);