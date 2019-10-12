SET NAMES utf8;
SET time_zone = '+00:00';
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

-- you must create the database school_aop
USE `school_aop`;

DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

CREATE TABLE `users` (
  `id`       INT(11) UNSIGNED           NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255)
             COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled`  TINYINT NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `users` (`id`, `username`, `password`) VALUES
  (1, 'daniel', 'daniel'),
  (2, 'alex', 'alex'),
  (3, 'admin', 'admin');

CREATE TABLE `books` (
  `id`           INT(11) UNSIGNED           NULL     AUTO_INCREMENT,
  `title`        VARCHAR(255)
                 COLLATE utf8mb4_unicode_ci NOT NULL,
  `author`       VARCHAR(255)
                 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_publish` DATE                       NULL     DEFAULT NULL,
  `loaned_by`    INT UNSIGNED               NULL     DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `books_loaned_by` FOREIGN KEY (`loaned_by`) REFERENCES `users` (`id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


INSERT INTO `books` (`id`, `title`, `author`, `date_publish`, `loaned_by`) VALUES
  (1, 'The Girl Who Kicked the Hornet\'s Nest ', 'Stieg Larsson', '2010-05-25', 1),
  (2, 'The Hunger Games', 'Suzanne Collins', '2008-07-26', 1),
  (3, 'The Girl on the Train: A Novel', ' Paula Hawkins', '2015-01-15', NULL),
  (4, 'The Great Gatsby', 'F. Scott Fitzgerald ', '1925-01-19', NULL),
  (5, 'Ulysses', 'James Joyce', '1922-06-15', NULL),
  (6, 'Invisible Man ', 'Ralph Ellison', '1975-05-12', NULL),
  (7, 'Brave New World', 'Aldous Huxley', '1932-01-01', NULL),
  (8, 'Catch-22', 'Joseph Heller', '1961-11-11', NULL),
  (9, 'The Grapes of Wrath', 'John Steinbeck', '1939-07-15', NULL),
  (10, 'Darkness at Noon', 'Arthur Koestler', '1940-05-25', NULL);


-- TODO, add many to many from books to users
CREATE TABLE `loans` (
  `book_id` INT(11) NOT NULL,
  UNIQUE KEY `book_id` (`book_id`),
  CONSTRAINT `constraint_book_id` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;



