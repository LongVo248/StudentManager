-- noinspection SqlNoDataSourceInspectionForFile

-- DROP TABLE IF EXISTS countries;
--
-- CREATE TABLE countries (
--                           id INT AUTO_INCREMENT  PRIMARY KEY,
--                           name1 VARCHAR(250) NOT NULL
--
-- );
DROP TABLE IF EXISTS student;
CREATE TABLE student (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(45) NOT NULL,
                           email VARCHAR(45) NOT NULL,
                           address VARCHAR(45) NOT NULL,
                           dob DATE NOT NULL,
                           phone VARCHAR(11) NOT NULL
);