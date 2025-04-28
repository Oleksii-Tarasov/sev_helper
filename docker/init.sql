CREATE TABLE Sev_Users (
    id SERIAL PRIMARY KEY,
    edrpou VARCHAR(10) NOT NULL UNIQUE,
    short_name VARCHAR(255),
    full_name VARCHAR(255),
    is_terminated VARCHAR(30),
    is_connected BOOLEAN DEFAULT FALSE
);

CREATE TABLE DocFlow_Users (
    id SERIAL PRIMARY KEY,
    edrpou VARCHAR(10) NOT NULL UNIQUE
);

CREATE INDEX idx_sev_users_edrpou ON Sev_Users(edrpou);
CREATE INDEX idx_docflow_sev_users_edrpou ON DocFlow_Users(edrpou);
