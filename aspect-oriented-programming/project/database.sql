SET NAMES utf8;
SET time_zone = '+00:00';
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

-- you must create the database school_aop
USE `school_aop_project`;

DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;

-- aka agents
CREATE TABLE `users` (
  `id`       INT UNSIGNED               NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255)
             COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` VARCHAR(255)
             COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled`  TINYINT                    NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `users` (`id`, `username`, `password`) VALUES
  (1, 'daniel', 'daniel'),
  (2, 'gabi', 'gabi'),
  (3, 'admin', 'admin');

CREATE TABLE `products` (
  `id`       INT UNSIGNED               NULL     AUTO_INCREMENT, -- aka product_code, is unique
  `name`     VARCHAR(255)
             COLLATE utf8mb4_unicode_ci NOT NULL,
  `quantity` INT UNSIGNED               NULL     DEFAULT NULL, -- available quantity, this should be always constant
  `price`    FLOAT                      NULL     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `products` (`id`, `name`, `quantity`, `price`) VALUES
  (1,	'Roast Beef',	20,	2.0),
  (2,	'French Cheese',	10,	20.0);

CREATE TABLE `orders` (
  `id`         INT UNSIGNED NOT NULL AUTO_INCREMENT, -- an agent can place multiple orders
  `user_id`    INT UNSIGNED NOT NULL,
  `product_id` INT UNSIGNED NOT NULL,
  `quantity`   INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `constraint_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `constraint_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
