-- Daniel Butum - G921

-- get all issues that have a milestone
-- get all issues that have a specific milestone
-- DONE get all the issues a user has commented on (DISTINCT)

-- get the user page profile of user, with all the starred repositories and their names

-- get a issue with all comments of an issue

-- get all the users who stared a project

-- get all the snippets of a project created by it's creator

-- DONE group by the total stars of each project, top 5 starred projects
-- send emails for beta testing feature for a project

-- issue + user + issue comments + project
-- issue + milestone + label
-- user + emails
-- snippets + project + user
-- 

-- select all the users with emails
SELECT U.id, E.email
FROM users U
LEFT JOIN emails E
	ON U.id = E.user_id
WHERE U.id = '2'

-- 1. top 5 starred projects with at least 2 stars
SELECT TOP 5 S.project_id, P.name, MIN(P.[description]) AS [description], MIN(U.username) AS creator_name, COUNT(*) AS count_stars
	FROM users_star_projects S
	LEFT JOIN projects P
		ON P.id = S.project_id
	INNER JOIN users U
		ON P.creator_id = U.id
	GROUP BY S.project_id, P.name
	HAVING COUNT(*) > 1
	ORDER BY count_stars DESC

-- 2. get all the issues a user has commented on
SELECT DISTINCT IC.issue_id, I.title
	FROM issue_comments IC
	INNER JOIN users U
		ON IC.author_id = U.id
	INNER JOIN issues I
		ON IC.issue_id = I.id
	WHERE IC.author_id = '11'

-- 3. get all the projects that are bug free, no issues
SELECT P.id, P.name, P.[description], SC.count_stars FROM (
		SELECT P.id -- get all the projects that do not have any issues
			FROM projects P
			WHERE P.id NOT IN (SELECT DISTINCT project_id FROM issues)
		UNION
		SELECT DISTINCT I.project_id -- get all the projects that have all the issues closed
			FROM issues I
			WHERE I.[state] = '0'
	) as T -- get all the bug free projects
	LEFT JOIN projects P
		ON P.id = T.id
	LEFT JOIN (
		SELECT SP.project_id AS id, COUNT(*) AS count_stars -- count all the stars
		FROM users_star_projects SP
		GROUP BY SP.project_id
	) SC on SC.id = P.id

-- 4. get an issue, with all the data, including milestones
SELECT U.username, I.title, I.[description], I.branch_name, UP.project_access, M.title AS milestone
	FROM issues I
	INNER JOIN projects P -- get the project info
		ON I.project_id = P.id
	INNER JOIN users U -- get the user info
		ON I.author_id = U.id
	LEFT JOIN users_projects UP -- get the author project access
		ON I.author_id = UP.user_id AND I.project_id = UP.project_id
	LEFT JOIN milestones M -- get the milestone
		ON I.milestone_id = M.id
	WHERE I.id = '4'

-- 5. get all comments of an issue, with the project access
SELECT IC.author_id, U.username, IC.[description], IC.created_at, UP.project_access
	FROM issue_comments IC
	INNER JOIN users U -- get the user info
		ON IC.author_id = U.id
	INNER JOIN issues I
		ON IC.issue_id = I.id
	LEFT JOIN users_projects UP -- N-N get the project access
		ON IC.author_id = UP.user_id AND I.project_id = UP.project_id
	WHERE IC.issue_id = '2'
	ORDER BY IC.created_at DESC

-- 6. get the overview of all the issues of an project
SELECT I.id, I.title, U.username, C.count_comments, M.title AS milestone
	FROM issues I
	LEFT JOIN ( -- count comments
		SELECT issue_id AS id, COUNT(*) as count_comments
		FROM issue_comments -- N - N
		GROUP BY issue_id
	) C ON I.id = C.id
	INNER JOIN users U -- get author info
		ON I.author_id = U.id
	LEFT JOIN milestones M -- get milestone info
		ON I.milestone_id = M.id
	WHERE I.project_id = '2'

-- 7. get all projects that are alive
-- get all projects with at least 2 people working on them and with at least 1 label defined
SELECT P.id, P.[path], P.[description], SC.count_stars
	FROM projects P
	INNER JOIN ( -- get all projects with at least 2 contributors
		SELECT UP.project_id AS id
			FROM users_projects UP -- N - N
			GROUP BY UP.project_id
			HAVING COUNT(*) >= 2
	) UP ON UP.id = P.id
	INNER JOIN (  -- count all the stars
		SELECT SP.project_id AS id, COUNT(*) AS count_stars
			FROM users_star_projects SP -- N - N
			GROUP BY SP.project_id
	) SC on SC.id = P.id
	INNER JOIN ( -- all the projects with at least 1 label
		SELECT I.project_id AS id
			FROM issues I
			INNER JOIN labels_links LL -- N - N
				ON I.id = LL.issue_id
			INNER JOIN labels L
				ON LL.label_id = L.id
			GROUP BY I.project_id
			HAVING COUNT(*) > 0
	) LC ON LC.id = P.id

SELECT P.id, P.[path], P.[description], SC.count_stars
	FROM projects P
	INNER JOIN ( -- get all projects with at least 2 contributors
		SELECT UP.project_id
			FROM users_projects UP -- N - N
			GROUP BY UP.project_id
			HAVING COUNT(*) >= 2
	) UP ON UP.project_id = P.id
	LEFT JOIN (  -- count all the stars
		SELECT SP.project_id AS id, COUNT(*) AS count_stars
			FROM users_star_projects SP -- N - N
			GROUP BY SP.project_id
	) SC on SC.id = P.id

-- 8. 
-- send emails for beta testing feature for a project
-- send notice email to all the users who starred a project
SELECT E.email
	FROM users U
	INNER JOIN ( -- select all the users who starred that project
		SELECT user_id AS id
			FROM users_star_projects -- N - N
			WHERE project_id = '3'
	) US ON U.id = US.id
	INNER JOIN emails E -- N - N select email
		ON U.id = E.user_id

-- 9. Get all the labels of an issue that has a project with
SELECT *
	FROM issues I
	INNER JOIN labels_links LL
		ON I.id = LL.issue_id
	INNER JOIN labels L
		ON LL.label_id = L.id

-- 10. Get all the snippets of a user that has a project attached to it and the project has at least 1 stars
-- and the user has access to it
-- aka filter
SELECT *
	FROM snippets S -- N - N
	INNER JOIN projects P
		ON S.project_id = P.id
	INNER JOIN (
	SELECT SP.project_id AS id, COUNT(*) AS count_stars -- count all the stars
		FROM users_star_projects SP -- N - N
		GROUP BY SP.project_id
		HAVING COUNT(*) > 0
	) CS ON CS.id = S.project_id
	INNER JOIN users U
		ON S.author_id = U.id
	INNER JOIN users_projects USP -- N - N
		ON USP.user_id = S.author_id AND USP.project_id = S.project_id
	WHERE S.project_id IS NOT NULL AND S.author_id = '3'


SELECT *
	FROM snippets S
	INNER JOIN projects P
		ON S.project_id = P.id
	INNER JOIN users U
		ON S.author_id = U.id
	WHERE S.project_id IS NOT NULL AND S.author_id = '3'
