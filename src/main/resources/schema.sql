-- Drop the 'employee' table if it exists
DROP TABLE IF EXISTS `employee`;

-- Create the 'employee' table
CREATE TABLE IF NOT EXISTS `employee`
(
    `id`         INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `first_name` VARCHAR(50),
    `last_name`  VARCHAR(50),
    `city_name`  VARCHAR(50)
);

-- Drop the 'department' table if it exists
DROP TABLE IF EXISTS `department`;

-- Create the 'department' table
CREATE TABLE IF NOT EXISTS `department`
(
    `id`        INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`      VARCHAR(50),
    `city_name` VARCHAR(50)
);

-- Count of 'employee' table
SELECT COUNT(*)
FROM `employee`;

-- Count of 'department' table
SELECT COUNT(*)
FROM `department`;
