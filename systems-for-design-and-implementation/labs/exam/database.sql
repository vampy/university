SET NAMES utf8;
SET time_zone = '+00:00';
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

-- you must create the database school_sdi
USE `school_sdi`;

DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

CREATE TABLE `books` (
  `id`           INT(11) UNSIGNED           NOT NULL AUTO_INCREMENT,
  `title`        VARCHAR(255)
                 COLLATE utf8mb4_unicode_ci NOT NULL,
  `author`       VARCHAR(255)
                 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_publish` DATE                                DEFAULT NULL,
  `stock`        INT(10) UNSIGNED           NOT NULL DEFAULT '1'
  COMMENT 'Numbe of books of this kind',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `books` (`id`, `title`, `author`, `date_publish`, `stock`) VALUES
  (1, 'The Girl Who Kicked the Hornet\'s Nest ', 'Stieg Larsson', '2010-05-25', 1),
  (2, 'The Hunger Games', 'Suzanne Collins', '2008-07-26', 2),
  (3, 'The Girl on the Train: A Novel', ' Paula Hawkins', '2015-01-15', 1),
  (4, 'The Great Gatsby', 'F. Scott Fitzgerald ', '1925-01-19', 3),
  (5, 'Ulysses', 'James Joyce', '1922-06-15', 2),
  (6, 'Invisible Man ', 'Ralph Ellison', '1975-05-12', 1),
  (7, 'Brave New World', 'Aldous Huxley', '1932-01-01', 1),
  (8, 'Catch-22', 'Joseph Heller', '1961-11-11', 22),
  (9, 'The Grapes of Wrath', 'John Steinbeck', '1939-07-15', 5),
  (10, 'Darkness at Noon', 'Arthur Koestler', '1940-05-25', 1);

CREATE TABLE `users` (
  `id`           INT(11) UNSIGNED           NOT NULL AUTO_INCREMENT,
  `username`     VARCHAR(255)
                 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password`     VARCHAR(255)
                 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `users` (`id`, `username`, `password`, `is_librarian`) VALUES
  (1, 'daniel', 'daniel', 0),
  (2, 'alex', 'alex', 0),
  (3, 'admin', 'admin', 1),
  (4, 'admin2', 'admin2', 1);

CREATE TABLE `loans` (
  `user_id`   INT(11) UNSIGNED NOT NULL,
  `book_id`   INT(11) UNSIGNED NOT NULL,
  `date_loan` TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `book_id`),
  KEY `fk_book_id` (`book_id`),
  CONSTRAINT `fk_book_id` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;

INSERT INTO `loans` (`user_id`, `book_id`, `date_loan`) VALUES
  (1, 1, '2016-04-03 10:27:19'),
  (1, 2, '2016-04-03 10:32:42');







