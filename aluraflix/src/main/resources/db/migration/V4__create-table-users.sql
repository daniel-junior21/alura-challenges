CREATE TABLE users(
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,

    PRIMARY KEY(id)
);

INSERT INTO users VALUES (1, "admin", "$2a$12$dkhAHosZZ9.ysUeoXRjlTuepSccvsGl2gTv9aBNP2Io8fo3EvR.PW");