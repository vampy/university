USE test;

-- ALTER TABLE snippets 
--	ALTER COLUMN title NVARCHAR(255);

-- sys.objects sys.columns sys.types

SELECT *
FROM sys.objects
WHERE type = 'F';


SELECT
    O.name,
    T.name,
    O.object_id,
    C.name
FROM sys.objects O
    INNER JOIN sys.columns C
        ON O.object_id = C.object_id
    INNER JOIN sys.types T
        ON C.user_type_id = T.user_type_id
WHERE type = 'U' AND O.name = 'users';

ALTER TABLE snippets
ADD CONSTRAINT df_snippets_title DEFAULT 'EMPTY TITLE' FOR title;

-- ALTER TABLE snippets
--	DROP CONSTRAINT df_snippets_title;


--ALTER TABLE users
--	ADD skype_name VARCHAR(255);

ALTER TABLE users
DROP COLUMN skype_name;


-- projects milestone_id

-- ALTER TABLE issues
--	DROP CONSTRAINT FK__issues__mileston__1C5231C2;

ALTER TABLE issues
ADD CONSTRAINT issues_milestone_pk FOREIGN KEY (milestone_id) REFERENCES milestones (id);


CREATE PROCEDURE get_users_by_id @user_id INT
AS
    SELECT *
    FROM users
    WHERE id = @user_id;
    EXEC get_users_by_id(user_id = '1');


    SELECT
        P.id,
        P.name,
        P.[description],
        COUNT(SP.project_id) AS star_count
    FROM (
             SELECT P.id -- get all the projects that do not have any issues
             FROM projects P
             WHERE P.id NOT IN (SELECT DISTINCT project_id
                                FROM issues)
             UNION
             SELECT DISTINCT I.project_id -- get all the projects that have all the issues closed
             FROM issues I
             WHERE I.[state] = '0'
         ) AS T
        LEFT JOIN projects P
            ON P.id = T.id
        LEFT JOIN users_star_projects SP
            ON P.id = SP.project_id
    GROUP BY P.id, P.name, P.[description]
