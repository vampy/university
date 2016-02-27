USE test;

-- create views
-- a view with a select statement working on a single table,
IF OBJECT_ID('view_users_all', 'V') IS NOT NULL
    DROP VIEW view_users_all
GO
CREATE VIEW view_users_all AS
SELECT *
FROM users

-- a view with a select statement working on at least two tables,
IF OBJECT_ID('view_users_all_emails', 'V') IS NOT NULL
    DROP VIEW view_users_all_emails
GO
CREATE VIEW view_users_all_emails AS
SELECT U.id, E.email
FROM users U
LEFT JOIN emails E
    ON U.id = E.user_id

-- a view with a select statement working on at least two tables and having a group by clause.
IF OBJECT_ID('view_projects_stars', 'V') IS NOT NULL
    DROP VIEW view_projects_stars
GO
CREATE VIEW view_projects_stars AS
SELECT S.project_id, MAX(P.name) AS [name], MIN(P.[description]) AS [description], COUNT(*) AS count_stars
    FROM users_star_projects S
    LEFT JOIN projects P
        ON P.id = S.project_id
    GROUP BY S.project_id
    HAVING COUNT(*) > 1


-- a table with one column as the primary key and no foreign key,
IF OBJECT_ID('20*users', 'P') IS NOT NULL
    DROP PROCEDURE [20*users]
GO
CREATE PROCEDURE [20*users]
AS
    INSERT INTO users(username, password_hash, name, created_at, updated_at)
    VALUES
    ('test_sashareynolds3kQ', 'YJFJ4n79tVgPHsn', 'Sasha Reynolds', '1990-10-16', '1990-10-16'),
    ('test_ceciliahicksRgZ_test', 'AQPSXw46dnhTguC', 'Cecilia Hicks', '1967-02-02', '1967-02-02'),
    ('test_pearlhobbswCz_test', 'jQGPGYbVnNPfuvz', 'Pearl Hobbs', '1952-01-25', '1952-01-25'),
    ('test_alisonfosterRdU_test', '5j3KdCxR43yk6uN', 'Alison Foster', '1960-11-09', '1960-11-09'),
    ('test_islaboltonq9W_test', 'BXZ2p2dpcuz5QFy', 'Isla Bolton', '1977-12-17', '1977-12-17'),
    ('test_madalynchenPCF_test', 'Sv9Ba4w3cn87SyP', 'Madalyn Chen', '1993-02-01', '1993-02-01'),
    ('test_aidanwintersa8k_test', '23r25DEKhUFJGxW', 'Aidan Winters', '1984-04-17', '1984-04-17'),
    ('test_sashareynolds3kQ_test', 'YJFJ4n79tVgPHsn', 'Sasha Reynolds', '1990-10-16', '1990-10-16'),
    ('test_remingtonmccallxJp_test', 'Z5XmeeuHm9dVDvA', 'Remington Mccall', '1972-01-01', '1972-01-01'),
    ('test_korbinbonner8UR_test', 'qKH66WHY3z6S8h3', 'Korbin Bonner', '1967-10-27', '1967-10-27');

    DELETE FROM users
    WHERE id IN (
        select top 10 id
        from users
        order by id desc
    )
GO

-- a table with one column as the primary key and at least one foreign key,
IF OBJECT_ID('40*emails', 'P') IS NOT NULL
    DROP PROCEDURE [40*emails]
GO
CREATE PROCEDURE [40*emails]
AS
    INSERT INTO emails(user_id, email)
        VALUES
        ('1', 'test_atmitchellburkewPT@example.com'),
        ('2', 'test_jazminstephensSHs@example.com'),
        ('2', 'test_jazminstephensSHs@gmail.com.com'),
        ('3', 'test_brookssanfordVUf@example.com'),
        ('4', 'test_kellanboyerHgd@example.com'),
        ('4', 'test_kellanboyerHgd@ubblcluj.ro'),
        ('5', 'test_dylandiazC3p@example.com'),
        ('6', 'test_dixietorresBP3@example.com'),
        ('7', 'test_larissamcintoshQuT@example.com'),
        ('8', 'test_aliaforbes2YM@example.com'),
        ('9', 'test_genevieveblanchardUA8@example.com'),
        ('10', 'test_jadadodson2zv@example.com'),
        ('10', 'test_jadadodson2zv@hotmail.com'),
        ('10', 'test_jadadodson2zv@yahoo.com'),
        ('11', 'test_aylabarrerabsF@example.com'),
        ('12', 'test_skyemillsbnc@example.com'),
        ('13', 'test_sashareynolds3kQ@example.com'),
        ('14', 'test_ceciliahicksRgZ@example.com'),
        ('14', 'test_ceciliahicksRgZ@yahoo.com'),
        ('15', 'test_pearlhobbswCz@example.com');

    DELETE FROM emails
    WHERE id IN (
        SELECT TOP 20 id
        FROM emails
        ORDER BY id DESC
    )
GO

-- a table with two columns as the primary key,
IF OBJECT_ID('10*users_star_projects', 'P') IS NOT NULL
    DROP PROCEDURE [10*users_star_projects]
GO
CREATE PROCEDURE [10*users_star_projects]
AS
    INSERT INTO users_star_projects(user_id, project_id)
        VALUES
        ('6', '1'),
        ('6', '2'),
        ('6', '3'),
        ('6', '4'),
        ('6', '5');

    DELETE FROM users_star_projects
    WHERE user_id = '6';
GO
