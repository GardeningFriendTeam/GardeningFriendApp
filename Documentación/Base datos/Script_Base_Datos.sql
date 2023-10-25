-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Android.project
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Android.project
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Android.project` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema android.project
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema android.project
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `android.project` DEFAULT CHARACTER SET utf8 ;
USE `Android.project` ;

-- -----------------------------------------------------
-- Table `Android.project`.`rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Android.project`.`rol` (
  `id_rol` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_rol`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Android.project`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Android.project`.`usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `nombre_usu` VARCHAR(45) NOT NULL,
  `contraseña` VARCHAR(10) NOT NULL,
  `foto` BLOB NOT NULL,
  `correo` VARCHAR(30) NOT NULL,
  `rol_id_rol` INT NOT NULL,
  PRIMARY KEY (`id_usuario`, `rol_id_rol`),
  INDEX `fk_usuario_rol_idx` (`rol_id_rol` ASC),
  CONSTRAINT `fk_usuario_rol`
    FOREIGN KEY (`rol_id_rol`)
    REFERENCES `Android.project`.`rol` (`id_rol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Android.project`.`cultivo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Android.project`.`cultivo` (
  `id_cultivo` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(45) NOT NULL,
  `imagen` BLOB NULL,
  `region` VARCHAR(45) NULL,
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
  `id_usuario_cultivo` INT NULL,
  PRIMARY KEY (`id_cultivo`),
  INDEX `fk_id_usuario_cultivo_idx` (`id_usuario_cultivo` ASC),
  CONSTRAINT `fk_id_usuario_cultivo`
    FOREIGN KEY (`id_usuario_cultivo`)
    REFERENCES `Android.project`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Android.project`.`favoritos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Android.project`.`favoritos` (
  `id_favoritos` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `tipo` VARCHAR(45) NOT NULL,
  `cultivo_favorito` INT NULL,
  PRIMARY KEY (`id_favoritos`),
  INDEX `fk_cultivo_favorito_idx` (`cultivo_favorito` ASC),
  CONSTRAINT `fk_cultivo_favorito`
    FOREIGN KEY (`cultivo_favorito`)
    REFERENCES `Android.project`.`cultivo` (`id_cultivo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Android.project`.`mis notas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Android.project`.`mis notas` (
  `id_notas` INT NOT NULL,
  `título` VARCHAR(45) NOT NULL,
  `contenido` VARCHAR(45) NOT NULL,
  `notas_favoritas` INT NULL,
  PRIMARY KEY (`id_notas`),
  INDEX `fk_notas_favoritas_idx` (`notas_favoritas` ASC),
  CONSTRAINT `fk_notas_favoritas`
    FOREIGN KEY (`notas_favoritas`)
    REFERENCES `Android.project`.`favoritos` (`id_favoritos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `android.project` ;

-- -----------------------------------------------------
-- Table `android.project`.`cultivo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `android.project`.`cultivo` (
  `id_cultivo` INT(11) NOT NULL AUTO_INCREMENT,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `android.project`.`rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `android.project`.`rol` (
  `id_rol` INT(11) NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_rol`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `android.project`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `android.project`.`usuario` (
  `id_usuario` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre_usu` VARCHAR(45) NOT NULL,
  `contraseña` VARCHAR(10) NOT NULL,
  `foto` BLOB NOT NULL,
  `correo` VARCHAR(30) NOT NULL,
  `rol_id_rol` INT(11) NOT NULL,
  PRIMARY KEY (`id_usuario`, `rol_id_rol`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
