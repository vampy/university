-- Adminer 4.2.2 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP TABLE IF EXISTS `countries`;
CREATE TABLE `countries` (
    `id`   INT(10) UNSIGNED        NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(64)
           COLLATE utf8_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8
    COLLATE = utf8_unicode_ci;

INSERT INTO `countries` (`id`, `name`) VALUES
    (3, 'France'),
    (4, 'Japan'),
    (1, 'Romania'),
    (5, 'Turkey'),
    (2, 'United Kingdom');

DROP TABLE IF EXISTS `destinations`;
CREATE TABLE `destinations` (
    `id`           INT(10) UNSIGNED        NOT NULL AUTO_INCREMENT,
    `name`         VARCHAR(128)
                   COLLATE utf8_unicode_ci NOT NULL,
    `description`  VARCHAR(2048)
                   COLLATE utf8_unicode_ci NOT NULL,
    `targets`      VARCHAR(2048)
                   COLLATE utf8_unicode_ci NOT NULL
    COMMENT 'A list of targets in this destination',
    `country_id`   INT(10) UNSIGNED        NOT NULL,
    `cost_per_day` INT(10) UNSIGNED        NOT NULL,
    PRIMARY KEY (`id`),
    KEY `country_id` (`country_id`),
    CONSTRAINT `destinations_ibfk_1` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8
    COLLATE = utf8_unicode_ci;


-- 2015-12-03 18:35:54

