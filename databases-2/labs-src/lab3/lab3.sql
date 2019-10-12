CREATE PROCEDURE insert_simple 
AS 
GO
CREATE PROCEDURE insert_advanced_savepoint
As
GO
CREATE PROCEDURE non_repeatable_read_1
AS
GO
CREATE PROCEDURE non_repeatable_read_2
AS
GO
CREATE PROCEDURE dirty_read_1
AS
GO
CREATE PROCEDURE dirty_read_2
AS
GO
CREATE PROCEDURE phantom_read_1
AS
GO
CREATE PROCEDURE phantom_read_2
AS
GO
CREATE PROCEDURE deadlock_1
AS
GO
CREATE PROCEDURE deadlock_2
AS
GO

---------------------------------------------------------------------------------------------------
ALTER PROCEDURE insert_simple
AS
	BEGIN TRY
		BEGIN TRANSACTION

		-- insert first table
		INSERT INTO users(username, password_hash, name, created_at, updated_at) 
			VALUES ('test_insert_simple', 'test_insert_simple', 'test_insert_simple', NULL, NULL)
		DECLARE @idu int
		SET @idu=(SELECT id FROM users Where username = 'test_insert_simple')

		-- insert second table
		INSERT INTO projects(creator_id, name, [description], [path], issues_enabled)
			VALUES(@idu, 'test_insert_simple_project', 'test_insert_simple_project', 'test_insert_simple_project', '1');

		DECLARE @idp int
		SET @idp=(SELECT id FROM projects Where name = 'test_insert_simple_project')
		
		-- simulate error
		RAISERROR ('RAISERROR in  insert_simple TRY block.', 16, 1);

		-- inset many to many
		INSERT INTO users_projects(project_id, user_id, project_access)
			VALUES (@idp, @idu, '0');

		COMMIT TRANSACTION
	END TRY
	BEGIN CATCH
		SELECT * FROM users;
		PRINT ERROR_MESSAGE()
		ROLLBACK TRANSACTION
		PRINT 'Transaction insert_simple rolled back.'
		SELECT * FROM users;
	END CATCH
GO

EXEC insert_simple;
---------------------------------------------------------------------------------------------------

-- TODO repair
ALTER PROCEDURE insert_advanced_savepoint
AS
	DECLARE @TranCounter INT = 0;
	BEGIN TRY
		BEGIN TRANSACTION
		SET @TranCounter = @@TRANCOUNT;
		-- insert first table
		INSERT INTO users(username, password_hash, name, created_at, updated_at) 
			VALUES ('test_insert_simple', 'test_insert_simple', 'test_insert_simple', NULL, NULL)
		SAVE TRANSACTION user_inserted
		DECLARE @idu int
		SET @idu=(SELECT id FROM users Where username = 'test_insert_simple')


		-- insert second table
		INSERT INTO projects(creator_id, name, [description], [path], issues_enabled)
			VALUES(@idu, 'test_insert_simple_project', 'test_insert_simple_project', 'test_insert_simple_project', '1');

		-- simulate error
		RAISERROR ('RAISERROR in  insert_advanced_savepoint TRY block.', 16, 1);
		-- TODO maybe save project?

		DECLARE @idp int
		SET @idp=(SELECT id FROM projects Where name = 'test_insert_simple_project')

		-- inset many to many
		INSERT INTO users_projects(project_id, user_id, project_access)
			VALUES (@idp, @idu, '0');

		COMMIT TRANSACTION
	END TRY
	BEGIN CATCH
		SELECT * FROM projects;
		IF @TranCounter > 0
		BEGIN
			PRINT ERROR_MESSAGE()
			ROLLBACK TRANSACTION user_inserted
			COMMIT TRANSACTION
			PRINT 'Transaction user_inserted rolled back.'
		END
		ELSE
		BEGIN
			PRINT ERROR_MESSAGE()
			ROLLBACK TRANSACTION
			PRINT 'Transaction rolled back.'
		END
		SELECT * FROM projects;
	END CATCH
GO

EXEC insert_advanced_savepoint;
---------------------------------------------------------------------------------------------------

ALTER PROCEDURE non_repeatable_read_1 AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ COMMITTED -- default
	BEGIN TRANSACTION
		SELECT * FROM users WHERE username = 'mitchellburkewPT'
		WAITFOR DELAY '0:0:05'
		SELECT * FROM users WHERE username = 'mitchellburkewPT'
	COMMIT TRANSACTION
END

ALTER PROCEDURE non_repeatable_read_2 AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ COMMITTED -- default

	BEGIN TRANSACTION
		UPDATE users SET name = 'non_repeatable_read' WHERE username = 'mitchellburkewPT'
	COMMIT TRANSACTION
END

EXEC non_repeatable_read_1;
---------------------------------------------------------------------------------------------------

ALTER PROCEDURE dirty_read_1 AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
	BEGIN TRANSACTION
		SELECT * FROM users WHERE username = 'mitchellburkewPT'
		WAITFOR DELAY '0:0:05'
		SELECT * FROM users WHERE username = 'mitchellburkewPT'
		WAITFOR DELAY '0:0:03'
		SELECT * FROM users WHERE username = 'mitchellburkewPT'
	COMMIT TRANSACTION
END

ALTER PROCEDURE dirty_read_2 AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
	BEGIN TRANSACTION
		UPDATE users SET name = 'dirty_read_2' WHERE username = 'mitchellburkewPT'
		WAITFOR DELAY '0:0:05'
	ROLLBACK TRANSACTION
END

EXEC dirty_read_1;
---------------------------------------------------------------------------------------------------

ALTER PROCEDURE phantom_read_1 AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ 
	
	BEGIN TRANSACTION
		SELECT * FROM projects WHERE creator_id = '1'
		WAITFOR DELAY '0:0:05'
		SELECT * FROM projects WHERE creator_id = '1'
	COMMIT TRANSACTION
END

ALTER PROCEDURE phantom_read_2 AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
	
	BEGIN TRANSACTION
		INSERT INTO projects(creator_id, name, [description], [path], issues_enabled)
		VALUES ('1', 'phantom_read_2', 'phantom_read_2', 'phantom_read_2', '1')
	COMMIT TRANSACTION
END

EXEC phantom_read_1;
---------------------------------------------------------------------------------------------------

ALTER PROCEDURE deadlock_1 AS
BEGIN
	SET XACT_ABORT ON
	DECLARE @nr_tries INT = 3;
	SET TRANSACTION ISOLATION LEVEL READ COMMITTED

Retry:
	BEGIN TRY
		BEGIN TRANSACTIOn

		UPDATE users SET name='deadlock_1' WHERE username = 'mitchellburkewPT'
		WAITFOR DELAY '0:0:05'
		UPDATE projects SET name='deadlock_1' WHERE creator_id = 1

		COMMIT TRANSACTION
	END TRY
	BEGIN CATCH
		ROLLBACK TRANSACTION
		PRINT '1 : Transaction rolled back.'

		IF @nr_tries > 0
		BEGIN
			PRINT '1 : Retry transaction in 3 seconds (' + CONVERT(CHAR(2), @nr_tries) + '/' + '2)'
			WAITFOR DELAY '0:0:03'
			SET @nr_tries = @nr_tries - 1
			GOTO Retry
		END
	END CATCH
END

ALTER PROCEDURE deadlock_2 AS
BEGIN
	SET XACT_ABORT ON
	DECLARE @nr_tries INT = 3;
	SET TRANSACTION ISOLATION LEVEL READ COMMITTED

Retry:
	BEGIN TRY
		BEGIN TRANSACTIOn

		UPDATE projects SET name='deadlock_2' WHERE creator_id = 1
		WAITFOR DELAY '0:0:05'
		UPDATE users SET name='deadlock_2' WHERE username = 'mitchellburkewPT'

		COMMIT TRANSACTION
	END TRY
	BEGIN CATCH
		ROLLBACK TRANSACTION
		PRINT '2 : Transaction rolled back.'

		IF @nr_tries > 0
		BEGIN
			PRINT '2 : Retry transaction in 3 seconds (' + CONVERT(CHAR(2), @nr_tries) + '/' + '2)'
			WAITFOR DELAY '0:0:03'
			SET @nr_tries = @nr_tries - 1
			GOTO Retry
		END
	END CATCH
END

EXEC deadlock_1;