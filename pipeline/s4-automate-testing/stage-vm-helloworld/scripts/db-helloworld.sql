SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema hellodb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `hellodb` ;

-- -----------------------------------------------------
-- Schema hellodb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hellodb` DEFAULT CHARACTER SET utf8 ;
USE `hellodb` ;

-- -----------------------------------------------------
-- Table `hellodb`.`TEST`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hellodb`.`TEST` ;

CREATE TABLE `TEST` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` char(10) DEFAULT NULL,
  `VAL` char(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;