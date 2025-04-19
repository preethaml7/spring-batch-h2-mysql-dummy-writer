DROP TABLE IF EXISTS employee;

CREATE TABLE IF NOT EXISTS `employee`
(
    id         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50),
    last_name  VARCHAR(50),
    city_name  VARCHAR(50)
);
COMMIT;

SELECT COUNT(*) FROM employee;