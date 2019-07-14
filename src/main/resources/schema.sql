
CREATE TABLE todo (
    id SERIAL PRIMARY KEY,
    tehtava varchar(255) NOT NULL,
    tarkempitehtava varchar(255),
    valmis BOOLEAN,
    aikataulu timestamp
);