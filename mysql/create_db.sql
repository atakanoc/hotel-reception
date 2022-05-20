CREATE DATABASE IF NOT EXISTS hotel;

CREATE TABLE `hotel`.`users` (
  `u_name` VARCHAR(30) NOT NULL,
  `u_password` VARCHAR(50) NULL,
  `u_is_admin` TINYINT NULL,
  PRIMARY KEY (`u_name`));

CREATE TABLE `hotel`.`room_type` (
  `t_name` VARCHAR(30) NOT NULL,
  `beds` SMALLINT NULL,
  `r_size` SMALLINT NULL,
  `has_view` TINYINT NULL,
  `has_kitchen` TINYINT NULL,
  `has_bathroom` TINYINT NULL, 
  `has_workspace` TINYINT NULL,
  `has_tv` TINYINT NULL,
  `has_coffee_maker` TINYINT NULL,
  PRIMARY KEY (`t_name`));

CREATE TABLE `hotel`.`room` (
  `r_num` SMALLINT NOT NULL,
  `r_floor` TINYINT NULL,
  `r_type` VARCHAR(30) NULL,
  PRIMARY KEY (`r_num`),
  FOREIGN KEY (`r_type`) REFERENCES room_type(`t_name`));

CREATE TABLE `hotel`.`booking` (
  `b_id` INT NOT NULL AUTO_INCREMENT,
  `r_num` SMALLINT NULL,
  `paid_by_card` TINYINT NULL,
  `b_from` DATE NOT NULL,
  `b_till` DATE NOT NULL,
  `b_fee` INT NULL,
  `b_is_paid` TINYINT NULL,
  `c_ss_number` INT NULL,
  PRIMARY KEY (`b_id`));

CREATE TABLE `hotel`.`customer` (
  `c_ss_number` INT NOT NULL,
  `c_address` VARCHAR(500) NULL,
  `c_full_name` VARCHAR(100) NULL,
  `c_phone_num` INT NOT NULL,
  `c_email` VARCHAR(100) NULL,
  PRIMARY KEY (`c_ss_number`));