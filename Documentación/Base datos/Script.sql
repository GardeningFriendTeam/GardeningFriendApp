-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Android.project
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `Android.project` ;

-- -----------------------------------------------------
-- Schema Android.project
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Android.project` DEFAULT CHARACTER SET utf8 ;
SHOW WARNINGS;
USE `Android.project` ;

-- -----------------------------------------------------
-- Table `rol`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `rol` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `rol` (
  `id_rol` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_rol`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `usuario` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `nombre_usu` VARCHAR(45) NOT NULL,
  `contraseña` VARCHAR(10) NOT NULL,
  `foto` BLOB NOT NULL,
  `correo` VARCHAR(30) NOT NULL,
  `rol_id_rol` INT NOT NULL,
  PRIMARY KEY (`id_usuario`, `rol_id_rol`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `cultivo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cultivo` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `cultivo` (
  `id_cultivo` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(45) NOT NULL,
  `imagen` BLOB NOT NULL,
  `region` VARCHAR(45) NOT NULL,
  `estacion de siembra` VARCHAR(45) NOT NULL,
  `tipo de suelo` VARCHAR(45) NOT NULL,
  `ph suelo` FLOAT NOT NULL,
  `temperatura recomendada` FLOAT NOT NULL,
  `riego` VARCHAR(45) NOT NULL,
  `luz solar` VARCHAR(45) NOT NULL,
  `luna` VARCHAR(45) NOT NULL,
  `profundidad` VARCHAR(45) NOT NULL,
  `altura maxima` VARCHAR(45) NOT NULL,
  `separacion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_cultivo`))
ENGINE = InnoDB;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
