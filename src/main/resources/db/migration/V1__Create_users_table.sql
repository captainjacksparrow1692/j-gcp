CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       full_name VARCHAR(255) NOT NULL,
                       address VARCHAR(500) NOT NULL,
                       phone_number VARCHAR(20) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       photo_url VARCHAR(500),
                       pinfl VARCHAR(14) NOT NULL UNIQUE,
                       age INTEGER NOT NULL,
                       document_type VARCHAR(50) NOT NULL,
                       issue_date DATE NOT NULL,
                       expiry_date DATE NOT NULL,
                       citizenship VARCHAR(100) NOT NULL,
                       death_date DATE,
                       CONSTRAINT chk_age CHECK (age >= 0 AND age <= 150),
                       CONSTRAINT chk_pinfl_digits_and_length CHECK (pinfl ~ '^[0-9]{14}$')
    );

CREATE INDEX idx_users_pinfl ON users(pinfl);
CREATE INDEX idx_users_email ON users(email);