
ALTER PROCEDURE insertRandom
@count_users INT = 10,
@count_projects INT = 10,
@count_issues INT = 10
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @i INT = 0, @j INT = 0, @k INT = 0;


	WHILE @i < @count_projects
	BEGIN
		DECLARE @project_name VARCHAR(10) = 'project-' + CONVERT(CHAR(2), @i),
			    @project_id INT = 0;
		-- PRINT @project_name

		-- add users
		SET @j = 0
		WHILE @j < @count_users
		BEGIN
			DECLARE @user_name VARCHAR(9) = CONVERT(varchar(255), NEWID()),
					@user_id INT = 0
			-- PRINT @user_name

			-- insert username
			INSERT INTO users(username, password_hash, name) VALUES(@user_name, 'random_password', 'Name-' + @user_name)
			SET @user_id = SCOPE_IDENTITY()


			-- project not set, insert first time project
			IF @project_id = 0
			BEGIN
				PRINT 'Setting project id'
				INSERT INTO projects(creator_id, name, [description], [path], issues_enabled) 
				VALUES(@user_id,  CONVERT(CHAR(2), @j) + @user_name + @project_name, 'random_description', '/dev/urandom/', 1)
				SET @project_id = SCOPE_IDENTITY()
			END

			-- insert into user_projects
			INSERT INTO users_projects(project_id, user_id, project_access) VALUES(@project_id, @user_id,  ABS(Checksum(NewID()) % 5))

			-- add issues
			SET @k = 0
			WHILE @k < @count_issues
			BEGIN
				INSERT INTO issues(project_id, author_id, milestone_id, title, [state])
				VALUES(@project_id, @user_id, NULL,  CONVERT(CHAR(3), ABS(Checksum(NewID()) % 999)) +  '-TITLE-' + @project_name + @user_name, ABS(Checksum(NewID()) % 5))

				SET @k = @k + 1
			END


			SET @j = @j + 1
		END

		SET @i = @i + 1
	END
	SET NOCOUNT OFF;
END
GO

EXEC insertRandom



SELECT  projects.name AS Project_Name, users.username AS User_Name, issues.title AS Issue_Title, issues.state AS State
FROM  projects INNER JOIN
	issues ON issues.project_id = projects.id INNER JOIN
	users ON issues.author_id = users.id INNER JOIN
	users_projects ON projects.id = users_projects.project_id AND users.id = users_projects.user_id
	WHERE users_projects.project_access = 0
	ORDER BY issues.title

-- for filter
IF EXISTS (SELECT name FROM sys.indexes WHERE name = N'idx_nc_user_projects_access')
	DROP INDEX idx_nc_user_projects_access ON users_projects;
CREATE NONCLUSTERED INDEX idx_nc_user_projects_access
	ON users_projects(project_access)

IF EXISTS (SELECT name FROM sys.indexes WHERE name = N'idx_nc_issues_state')
	DROP INDEX idx_nc_issues_state ON issues;
CREATE NONCLUSTERED INDEX idx_nc_issues_state
	ON issues(state)

-- for sort
IF EXISTS (SELECT name FROM sys.indexes WHERE name = N'idx_nc_issues_title')
	DROP INDEX idx_nc_issues_title ON issues;
CREATE NONCLUSTERED INDEX idx_nc_issues_title
	ON issues(title)

IF EXISTS (SELECT name FROM sys.indexes WHERE name = N'idx_c_issues_id_title')
	DROP INDEX idx_c_issues_id_title ON issues;
CREATE INDEX idx_c_issues_id_title
	ON issues(author_id, project_id) INCLUDE (title)