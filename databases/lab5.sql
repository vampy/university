USE test;

-- check if date is in range
-- remove trailing left, right 
-- check if project access is invalid

IF OBJECT_ID('is_project_access', 'F') IS NOT NULL
	DROP FUNCTION is_project_access;
GO
IF OBJECT_ID('TRIM', 'F') IS NOT NULL
	DROP FUNCTION TRIM;
GO
IF OBJECT_ID('format_users_name', 'F') IS NOT NULL
	DROP FUNCTION format_users_name;
GO
CREATE FUNCTION is_project_access(@access INT)
RETURNS BIT
AS
BEGIN
	DECLARE @ret BIT

	IF @access > 0 AND (@access = 0 OR @access = 1)
		SET @ret = 1
	ELSE
		SET @ret = 0

	RETURN @ret
END

CREATE FUNCTION TRIM(@string VARCHAR(1024))
RETURNs VARCHAR(1024)
AS
BEGIN
	DECLARE @return VARCHAR(1024) = LTRIM(RTRIM(@string))

	RETURN @string
END

CREATE FUNCTION format_users_name(@name VARCHAR(255), @username VARCHAR(255))
RETURNS VARCHAR(255)
AS
BEGIN
	EXEC @name = TRIM @name;
	IF LEN(@name) = 0
		RETURN @username

	RETURN @name
END


DECLARE @ret BIT;
EXEC @ret = is_project_acess @access = 4;
PRINT @ret

--DECLARE @flag INT
--EXEC usp_insert_user 'test', 'fdsfdsfds', 'test', @flag;
IF OBJECT_ID('usp_insert_user', 'P') IS NOT NULL
	DROP PROCEDURE usp_insert_user;
GO
IF OBJECT_ID('usp_update_user', 'P') IS NOT NULL
	DROP PROCEDURE usp_update_user;
GO
IF OBJECT_ID('usp_delete_user', 'P') IS NOT NULL
	DROP PROCEDURE usp_delete_user;
GO
CREATE PROCEDURE usp_insert_user
	@username VARCHAR(255),
	@password_hash VARCHAR(255),
	@name VARCHAR(255),
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		INSERT  INTO users(username, password_hash, name, created_at, updated_at)
		VALUES(@name, @password_hash, @name, GETDATE(), GETDATE());

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END

CREATE PROCEDURE usp_update_user
	@user_id INT,
	@username VARCHAR(255),
	@password_hash VARCHAR(255),
	@name VARCHAR(255),
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		UPDATE users
		SET
			username = @username,
			password_hash = @password_hash,
			name = @name,
			updated_at = GETDATE()
		WHERE id = @user_id

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END

CREATE PROCEDURE usp_delete_user
	@user_id INT,
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		DELETE FROM users WHERE id = @user_id;

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END


IF OBJECT_ID('usp_insert_email', 'P') IS NOT NULL
	DROP PROCEDURE usp_insert_email;
GO
IF OBJECT_ID('usp_update_email', 'P') IS NOT NULL
	DROP PROCEDURE usp_update_email;
GO
IF OBJECT_ID('usp_delete_email', 'P') IS NOT NULL
	DROP PROCEDURE usp_delete_email;
GO
CREATE PROCEDURE usp_insert_email
	@user_id INT,
	@email VARCHAR(255),
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		INSERT  INTO emails(user_id, email, created_at, updated_at)
		VALUES(@user_id, @email, GETDATE(), GETDATE());

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END

CREATE PROCEDURE usp_update_email
	@email_id INT,
	@email VARCHAR(255),
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		UPDATE emails
		SET
			email = @email,
			updated_at = GETDATE()
		WHERE id = @email_id

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END

CREATE PROCEDURE usp_delete_user
	@email_id INT,
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		DELETE FROM emails WHERE id = @email_id;

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END


IF OBJECT_ID('usp_insert_project', 'P') IS NOT NULL
	DROP PROCEDURE usp_insert_project;
GO
IF OBJECT_ID('usp_update_project', 'P') IS NOT NULL
	DROP PROCEDURE usp_update_project;
GO
IF OBJECT_ID('usp_delete_project', 'P') IS NOT NULL
	DROP PROCEDURE usp_delete_project;
GO
CREATE PROCEDURE usp_insert_project
	@creator_id INT,
	@name VARCHAR(255),
	@description VARCHAR(255),
	@path   VARCHAR(255),
	@is_issues BIT,
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		INSERT INTO projects(creator_id, name, [description], [path], issues_enabled, created_at, updated_at)
		VALUES(@creator_id, @name, @description, @path, @is_issues, GETDATE(), GETDATE());

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END

CREATE PROCEDURE usp_update_project
	@project_id INT,
	@description VARCHAR(255),
	@is_issues BIT,
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		UPDATE projects
		SET
			[description] = @description,
			issues_enabled = @is_issues,
			updated_at = GETDATE()
		WHERE id = @project_id

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END

CREATE PROCEDURE usp_delete_project
	@project_id INT,
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		DELETE FROM projects WHERE id = @project_id;

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END


IF OBJECT_ID('usp_insert_user_project', 'P') IS NOT NULL
	DROP PROCEDURE usp_insert_user_project;
GO
IF OBJECT_ID('usp_update_user_project', 'P') IS NOT NULL
	DROP PROCEDURE usp_update_user_project;
GO
IF OBJECT_ID('usp_delete_user_project', 'P') IS NOT NULL
	DROP PROCEDURE usp_delete_user_project;
GO
CREATE PROCEDURE usp_insert_user_project
	@user_id INT,
	@project_id INT,
	@access INT,
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	DECLARE @ret BIT;
	EXEC is_project_access @access, @ret;

	BEGIN TRY
		INSERT INTO users_projects(project_id, user_id, project_access, created_at, updated_at)
		VALUES(@project_id, @user_id, @access, GETDATE(), GETDATE());

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END

CREATE PROCEDURE usp_update_user_project
	@user_id INT,
	@project_id INT,
	@access INT,
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		UPDATE users_projects
		SET
			project_access = @access,
			updated_at = GETDATE()
		WHERE user_id = @user_id AND project_id = @project_id

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END

CREATE PROCEDURE usp_delete_user_project
	@user_id INT,
	@project_id INT,
	@return BIT OUTPUT
AS
BEGIN
BEGIN TRANSACTION
	BEGIN TRY
		DELETE FROM users_projects WHERE project_id = @project_id AND user_id = @user_id;

		SET @return = 1;
		IF @@TRANCOUNT > 0
		BEGIN
			COMMIT TRANSACTION;
		END
	END TRY
	BEGIN CATCH
		IF @@TRANCOUNT > 0
		BEGIN
			ROLLBACK TRANSACTION;
		END
		SET @return = 0;
	END CATCH
END







-- all users emails
IF OBJECT_ID('view_user_emails', 'V') IS NOT NULL
BEGIN
	DROP VIEW view_user_emails;
END
CREATE VIEW view_user_emails 
AS
SELECT U.username, E.email, E.created_at, E.updated_at
FROM users U
LEFT JOIN emails E 
	ON U.id = E.user_id;

IF OBJECT_ID('view_user_emails_index', 'V') IS NOT NULL
BEGIN
	DROP VIEW view_user_emails_index;
END
CREATE VIEW view_user_emails_index 
AS
SELECT U.username, E.email, E.created_at, E.updated_at
FROM users U
LEFT JOIN emails E 
	WITH (INDEX(idx_nc_emails_email))
	ON U.id = E.user_id;

SELECT * FROM view_user_emails WHERE email = 'jazminstephensSHs@example.com';
SELECT * FROM view_user_emails_index WHERE email = 'jazminstephensSHs@example.com';

IF EXISTS (SELECT name FROM sys.indexes WHERE name = N'idx_nc_emails_email')
	DROP INDEX idx_nc_emails_email ON emails;
CREATE NONCLUSTERED INDEX idx_nc_emails_email
	ON emails(email);


-- creator projects
IF OBJECT_ID('view_user_projects', 'V') IS NOT NULL
BEGIN
	DROP VIEW view_user_projects;
END
CREATE VIEW view_user_projects 
AS
SELECT U.id, P.name, P.created_at, P.updated_at
FROM projects P
INNER JOIN users U
	ON P.creator_id = U.id
WHERE P.issues_enabled = '1'

IF OBJECT_ID('view_user_projects_index', 'V') IS NOT NULL
BEGIN
	DROP VIEW view_user_projects_index;
END
CREATE VIEW view_user_projects_index 
AS
SELECT U.id, P.name, P.created_at, P.updated_at
FROM projects P
WITH (INDEX(idx_nc_projects_name))
INNER JOIN users U
	ON P.creator_id = U.id
WHERE P.issues_enabled = '1'

SELECT * FROM view_user_projects WHERE name = 'godot';
SELECT * FROM view_user_projects_index WHERE name = 'godot';

IF EXISTS (SELECT name FROM sys.indexes WHERE name = N'idx_nc_projects_name')
	DROP INDEX idx_nc_projects_name ON projects;
CREATE NONCLUSTERED INDEX idx_nc_projects_name
	ON projects(name)
