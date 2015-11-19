-- Daniel Butum - G921
DROP TABLE schema_current_version;
DROP TABLE schema_migrations;

-- Hold the current version number
CREATE TABLE schema_current_version(
	[version]		INT		NOT NULL PRIMARY KEY,	-- version number

	_one_row		BIT		DEFAULT '1' UNIQUE CHECK(_one_row = 1) -- make this table to be one row only
);
CREATE TABLE schema_migrations(
	id				INT				NOT NULL PRIMARY KEY IDENTITY(1, 1),
	upgrade_proc	VARCHAR(255)	NOT NULL,
	revert_proc		VARCHAR(255)	NOT NULL,
	[version]		INT				NOT NULL,		-- upgrade to this version, revert means to version - 1
);

-- Convention
-- <action> create, update, delete, 
-- <type> column, dconstraint, type
-- <table> 
-- <column>

-- add a column
IF OBJECT_ID('create_column_snippets_description', 'P') IS NOT NULL
	DROP PROCEDURE create_column_snippets_description
GO
CREATE PROCEDURE create_column_snippets_description
AS
	ALTER TABLE snippets
		ADD [description] VARCHAR(1024)	NOT NULL;
GO
IF OBJECT_ID('undo_create_column_snippets_description', 'P') IS NOT NULL
	DROP PROCEDURE undo_create_column_snippets_description
GO
CREATE PROCEDURE undo_create_column_snippets_description
AS
	ALTER TABLE snippets
		DROP COLUMN [description];
GO


-- add a default constraint
IF OBJECT_ID('create_dconstraint_labels_updated_at', 'P') IS NOT NULL
	DROP PROCEDURE create_dconstraint_labels_updated_at
GO
CREATE PROCEDURE create_dconstraint_labels_updated_at
AS
	ALTER TABLE labels
		ADD CONSTRAINT default_updated_at DEFAULT NULL FOR updated_at;
GO
IF OBJECT_ID('undo_create_dconstraint_labels_updated_at', 'P') IS NOT NULL
	DROP PROCEDURE undo_create_dconstraint_labels_updated_at
GO
CREATE PROCEDURE undo_create_dconstraint_labels_updated_at
AS
	ALTER TABLE labels
		DROP CONSTRAINT default_updated_at;
GO


-- modify the type of a column
IF OBJECT_ID('update_type_snippets_content', 'P') IS NOT NULL
	DROP PROCEDURE update_type_snippets_content
GO
CREATE PROCEDURE update_type_snippets_content
AS
	ALTER TABLE snippets
		ALTER COLUMN content VARCHAR(2048) NOT NULL;
GO
IF OBJECT_ID('undo_update_type_snippets_content', 'P') IS NOT NULL
	DROP PROCEDURE undo_update_type_snippets_content
GO
CREATE PROCEDURE undo_update_type_snippets_content
AS
	ALTER TABLE snippets
		ALTER COLUMN content TEXT NOT NULL;
GO


-- create/remove a foreign key constraint
IF OBJECT_ID('remove_fk_snippets_project_id', 'P') IS NOT NULL
	DROP PROCEDURE remove_fk_snippets_project_id
GO
CREATE PROCEDURE remove_fk_snippets_project_id
AS
	ALTER TABLE snippets
		DROP CONSTRAINT FK_snippets_project_id;
GO
IF OBJECT_ID('undo_remove_fk_snippets_project_id', 'P') IS NOT NULL
	DROP PROCEDURE undo_remove_fk_snippets_project_id
GO
CREATE PROCEDURE undo_remove_fk_snippets_project_id
AS
	ALTER TABLE snippets
		ADD CONSTRAINT FK_snippets_project_id FOREIGN KEY(author_id) REFERENCES users(id);
GO

-- create/remove a new table
IF OBJECT_ID('create_table_tags', 'P') IS NOT NULL
	DROP PROCEDURE create_table_tags
GO
CREATE PROCEDURE create_table_tags
AS
	CREATE TABLE tags(
		id		INT				NOT NULL PRIMARY KEY IDENTITY(1, 1),
		name	VARCHAR(255)	NOT NULL
	);
GO
IF OBJECT_ID('undo_create_table_tags', 'P') IS NOT NULL
	DROP PROCEDURE undo_create_table_tags
GO
CREATE PROCEDURE undo_create_table_tags
AS
	DROP TABLE tags;
GO

-- insert default version 1
INSERT INTO schema_current_version([version]) VALUES(1);

DELETE FROM schema_migrations;
INSERT INTO schema_migrations(upgrade_proc, revert_proc, [version])
	VALUES 
	('create_column_snippets_description', 'undo_create_column_snippets_description', 2),
	('create_dconstraint_labels_updated_at', 'undo_create_dconstraint_labels_updated_at', 3),
	('update_type_snippets_content', 'undo_update_type_snippets_content', 4),
	('remove_fk_snippets_project_id', 'undo_remove_fk_snippets_project_id', 5),
	('create_table_tags', 'undo_create_table_tags', 6);

IF OBJECT_ID('get_to_version', 'P') IS NOT NULL
	DROP PROCEDURE get_to_version
GO
CREATE PROCEDURE get_to_version
	@to_version INT
AS
	SET NOCOUNT ON;
	-- get current version
	DECLARE @current_version INT;
	-- UPDATE schema_current_version SET [version] = '42';
	SELECT @current_version = [version] FROM schema_current_version;

	-- we are already to that version number
	IF @current_version = @to_version
	BEGIN
		PRINT 'Database is already at that version';
		RETURN
	END

	-- declare used vars
	DECLARE @upgrade_proc NVARCHAR(255);
	DECLARE @revert_proc  NVARCHAR(255);
	DECLARE @version	  INT;
	IF @current_version < @to_version
	BEGIN
		PRINT('UPGRADING');

		DECLARE cursor_migrate CURSOR FOR SELECT upgrade_proc, [version] 
										FROM schema_migrations
										WHERE [version] > @current_version AND [version] <= @to_version
										ORDER BY [version] ASC;
		OPEN cursor_migrate;

		FETCH NEXT FROM cursor_migrate INTO @upgrade_proc, @version;
		WHILE @@FETCH_STATUS = 0
		BEGIN
			-- PRINT CONVERT(CHAR(2), @version) + '	' + @upgrade_proc;

			PRINT CHAR(13);
			PRINT 'EXECUTING: ' + @upgrade_proc;
			EXEC sp_executesql @upgrade_proc;

			IF @version > @current_version
			BEGIN
				PRINT 'Upgrading from version ' + CONVERT(CHAR(2), @current_version) + ' to version ' + CONVERT(CHAR(2), @version); 
				-- Update version
				SET @current_version = @current_version + 1;
				PRINT 'Database version is now ' + CONVERT(CHAR(2), @current_version)
			END

			FETCH NEXT FROM cursor_migrate INTO @upgrade_proc, @version;
		END
		UPDATE schema_current_version SET [version] = @to_version;

		CLOSE cursor_migrate;
		DEALLOCATE cursor_migrate;
	END
	ELSE
	BEGIN
		PRINT 'DOWNGRADING';

		DECLARE cursor_migrate CURSOR FOR SELECT revert_proc, [version] 
								FROM schema_migrations
								WHERE [version] > @to_version AND [version] <= @current_version
								ORDER BY [version] DESC;
		OPEN cursor_migrate

		FETCH NEXT FROM cursor_migrate INTO @revert_proc, @version
		WHILE @@FETCH_STATUS = 0
		BEGIN
			--PRINT CONVERT(CHAR(2), @version) + '	' + @revert_proc;
			PRINT CHAR(13);
			PRINT 'EXECUTING: ' + @revert_proc
			EXEC sp_executesql @revert_proc

			IF @version - 1 < @current_version
			BEGIN
				PRINT 'Downgrading from version ' + CONVERT(CHAR(2), @current_version) + ' to version ' + CONVERT(CHAR(2), @version - 1)
				-- Update version
				SET @current_version = @current_version - 1;
				PRINT 'Database version is now ' + CONVERT(CHAR(2), @current_version)
			END

			FETCH NEXT FROM cursor_migrate INTO @revert_proc, @version;
		END
		UPDATE schema_current_version SET [version] = @to_version;

		CLOSE cursor_migrate;
		DEALLOCATE cursor_migrate;
	END

	SET NOCOUNT OFF;
GO

EXEC get_to_version 1;
--exec sp_helpconstraint users;
