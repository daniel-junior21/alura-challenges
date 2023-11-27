ALTER TABLE videos ADD category BIGINT NOT NULL;
SET FOREIGN_KEY_CHECKS=0;
ALTER TABLE videos ADD
FOREIGN KEY (category) REFERENCES categories(id);

UPDATE videos set category = 1;
