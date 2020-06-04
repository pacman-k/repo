-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema interpol
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema interpol
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `interpol` DEFAULT CHARACTER SET utf8 ;
USE `interpol` ;

-- -----------------------------------------------------
-- Table `interpol`.`claim_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `interpol`.`claim_status` (
  `status_id` INT(11) NOT NULL AUTO_INCREMENT,
  `status_name` ENUM('under_consideration', 'actual', 'payment_waiting') NOT NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `status_name_UNIQUE` ON `interpol`.`claim_status` (`status_name` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `interpol`.`wallet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `interpol`.`wallet` (
  `wallet_id` INT(11) NOT NULL AUTO_INCREMENT,
  `wallet_value` DECIMAL(10,0) NULL DEFAULT '0',
  PRIMARY KEY (`wallet_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 158
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `wallet_id_UNIQUE` ON `interpol`.`wallet` (`wallet_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `interpol`.`user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `interpol`.`user_role` (
  `role_id` INT(11) NOT NULL AUTO_INCREMENT,
  `role_name` ENUM('admin', 'default') NOT NULL,
  PRIMARY KEY (`role_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `role_name_UNIQUE` ON `interpol`.`user_role` (`role_name` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `interpol`.`user_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `interpol`.`user_account` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `role_id` INT(11) NOT NULL,
  `wallet_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_account_wallet1`
    FOREIGN KEY (`wallet_id`)
    REFERENCES `interpol`.`wallet` (`wallet_id`),
  CONSTRAINT `userRole`
    FOREIGN KEY (`role_id`)
    REFERENCES `interpol`.`user_role` (`role_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 142
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `password_UNIQUE` ON `interpol`.`user_account` (`password` ASC) VISIBLE;

CREATE UNIQUE INDEX `id_UNIQUE` ON `interpol`.`user_account` (`id` ASC) VISIBLE;

CREATE UNIQUE INDEX `email_UNIQUE` ON `interpol`.`user_account` (`email` ASC) VISIBLE;

CREATE UNIQUE INDEX `login_UNIQUE` ON `interpol`.`user_account` (`login` ASC) VISIBLE;

CREATE UNIQUE INDEX `wallet_id_UNIQUE` ON `interpol`.`user_account` (`wallet_id` ASC) VISIBLE;

CREATE INDEX `userRole_idx` ON `interpol`.`user_account` (`role_id` ASC) VISIBLE;

CREATE INDEX `fk_user_account_wallet1_idx` ON `interpol`.`user_account` (`wallet_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `interpol`.`wanted_person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `interpol`.`wanted_person` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NULL DEFAULT 'unknown',
  `byname` VARCHAR(45) NULL DEFAULT 'unknown',
  `country` VARCHAR(45) NULL DEFAULT 'unknown',
  `birthday` TIMESTAMP NULL DEFAULT NULL,
  `description` VARCHAR(1000) NOT NULL,
  `photo` BLOB NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `id_UNIQUE` ON `interpol`.`wanted_person` (`id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `interpol`.`claim`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `interpol`.`claim` (
  `id` INT(11) NOT NULL,
  `date_of_claim` TIMESTAMP NOT NULL,
  `cost` DECIMAL(10,0) NULL DEFAULT '0',
  `status_id` INT(11) NOT NULL,
  `user_account_id` INT(11) NOT NULL,
  `wanted_person_id` INT(11) NOT NULL,
  `founder_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `claimesStatus`
    FOREIGN KEY (`status_id`)
    REFERENCES `interpol`.`claim_status` (`status_id`),
  CONSTRAINT `fk_claim_user_account1`
    FOREIGN KEY (`user_account_id`)
    REFERENCES `interpol`.`user_account` (`id`),
  CONSTRAINT `fk_claim_user_account2`
    FOREIGN KEY (`founder_id`)
    REFERENCES `interpol`.`user_account` (`id`),
  CONSTRAINT `fk_claim_wanted_person1`
    FOREIGN KEY (`wanted_person_id`)
    REFERENCES `interpol`.`wanted_person` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `wanted_person_id_UNIQUE` ON `interpol`.`claim` (`wanted_person_id` ASC) VISIBLE;

CREATE INDEX `claimesStatus_idx` ON `interpol`.`claim` (`status_id` ASC) VISIBLE;

CREATE INDEX `fk_claim_user_account1_idx` ON `interpol`.`claim` (`user_account_id` ASC) VISIBLE;

CREATE INDEX `fk_claim_wanted_person1_idx` ON `interpol`.`claim` (`wanted_person_id` ASC) VISIBLE;

CREATE INDEX `fk_claim_user_account2_idx` ON `interpol`.`claim` (`founder_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `interpol`.`news`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `interpol`.`news` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `date_of_post` TIMESTAMP NOT NULL,
  `news_text` VARCHAR(2000) NOT NULL,
  `news_topic` VARCHAR(45) NOT NULL,
  `news_heading` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
