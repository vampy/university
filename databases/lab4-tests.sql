USE test;
-- insert into tests(300*users_200*ViewSample) and views and tables
-- insert into testtables and testviews the tests that participate
-- test runs contain the big test

-- testtables, noofrows from the name of the procedure, position the position in the big tests: eg: 300*users is 0
-- testruns, descriptions is the id of the test, in our case it is 1, it has the big start and end
-- now we tokenize the big test
-- insert into testrunviews/testrunstables, the each individual procedure, and the testrun along side the small start and end

-- start big timer
-- while big loop

-- maybe here use testtable?
-- start small timer
-- execute
-- end small timer

-- end big loop
-- end big timer

SET NOCOUNT ON;
DELETE FROM TestRunViews;
DELETE FROM TestRunTables;
DELETE FROM TestRuns;
DELETE FROM TestViews;
DELETE FROM TestTables;
DELETE FROM Tests;
DELETE FROM [Tables];
DELETE FROM [Views];

DBCC CHECKIDENT('TestRuns', RESEED, 0);
DBCC CHECKIDENT('Tests', RESEED, 0);
DBCC CHECKIDENT('Tables', RESEED, 0);
DBCC CHECKIDENT('Views', RESEED, 0);

INSERT INTO [Tables](Name)
    VALUES
    ('users'),
    ('emails'),
    ('user_star_projects');

INSERT INTO [Views](Name)
    VALUES
    ('view_users_all'),
    ('view_users_all_emails'),
    ('view_projects_stars');

INSERT INTO Tests(Name)
    VALUES
    ('20*users-40*emails-view_users_all')
    --,('view_projects_stars-40*emails-10*users_star_projects');

INSERT INTO TestTables(TestID, TableID, NoOfRows, Position)
    VALUES
    ('1', '1', '20', '0'),
    ('1', '2', '40', '1');

INSERT INTO TestViews(TestID, ViewID)
    VALUES
    ('1', '1');

SET NOCOUNT OFF;
PRINT(CHAR(13)+CHAR(13));


IF OBJECT_ID('run_test', 'P') IS NOT NULL
    DROP PROCEDURE run_test
GO
CREATE PROCEDURE run_test
    @test_name            NVARCHAR(128),
    @test_tests_id        INT,
    @test_testruns_id   INT,
    @test_pos            INT
AS
    DECLARE @test_star_sep INT;
    DECLARE @test_start DATETIME;
    DECLARE @test_stop DATETIME;

    SET @test_star_sep = CHARINDEX('*', @test_name);
    IF @test_star_sep != 0 -- procedure
    BEGIN
        PRINT('Executing on table: ' + @test_name);
        IF OBJECT_ID(@test_name, 'P') IS NULL
        BEGIN
            PRINT('ERROR: Procedure does not exist = ' + @test_name);
            RETURN;
        END

        DECLARE @row_count  VARCHAR(16);
        DECLARE @table_name VARCHAR(64);
        DECLARE @table_id   INT;
        SET @row_count = SUBSTRING(@test_name, 1, @test_star_sep - 1);
        SET @table_name = SUBSTRING(@test_name, @test_star_sep + 1, LEN(@test_name))
        -- PRINT(@row_count + ' --- ' + @table);

        -- get table id
        SELECT @table_id = TableId
            FROM [Tables] WHERE Name = @table_name;
        IF @table_id IS NULL
        BEGIN
            PRINT('ERROR: does not exist in Tables, table = ' + @table_name);
            RETURN;
        END

        -- execute
        SET @test_start = SYSDATETIME();
        SET @test_name = N'[' + @test_name + N']'; -- escape procedure name
        EXEC @test_name;
        SET @test_stop = SYSDATETIME();
        INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt)
            VALUES (@test_testruns_id, @table_id, @test_start, @test_stop);
    END
    ELSE -- view
    BEGIN
        PRINT('Exectuting on view: ' + @test_name);
        IF OBJECT_ID(@test_name, 'V') IS NULL
        BEGIN
            PRINT('ERROR: View does not exist = ' + @test_name);
            RETURN;
        END

        DECLARE @view_id INT;

        -- get view id
        SELECT @view_id = ViewID
            FROM [Views] WHERE Name = @test_name;
        IF @view_id IS NULL
        BEGIN
            PRINT('ERROR: does not exist in Views, view = ' + @test_name);
            RETURN;
        END

        -- execute
        SET @test_start = SYSDATETIME();
        SET @test_name = N'SELECT * FROM ' + @test_name; -- use view
        EXEC sp_executesql @test_name;
        SET @test_stop = SYSDATETIME();
        INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt)
            VALUES (@test_testruns_id, @view_id, @test_start, @test_stop)
    END
GO

IF OBJECT_ID('run_tests', 'P') IS NOT NULL
    DROP PROCEDURE run_tests
GO
CREATE PROCEDURE run_tests
    @tests_name VARCHAR(1024)
AS
    SET NOCOUNT ON;
    PRINT('DEBUG: tests = ' + @tests_name);
    DECLARE @tests_start    DATETIME;
    DECLARE @tests_stop        DATETIME;
    DECLARE @tests_tests_id        INT;
    DECLARE @tests_testruns_id    INT;

    DECLARE @found_test_sep INT;
    DECLARE @found_test_pos INT;
    DECLARE @found_test        NVARCHAR(128);

    -- get id of the test
    SELECT @tests_tests_id = TestID
        FROM Tests WHERE Name = @tests_name;
    IF @tests_tests_id IS NULL
    BEGIN
        PRINT(N'ERROR: No test exists in the table Tests with the name = ' + @tests_name);
        RETURN;
    END

    -- start timer
    SET @tests_start = SYSDATETIME();
    INSERT INTO TestRuns([Description], StartAt) VALUES (@tests_name, @tests_start);
    SET @tests_testruns_id = SCOPE_IDENTITY(); -- get id of the testrun

    SET @found_test_pos = 0;
    WHILE CHARINDEX('-', @tests_name) > 0
    BEGIN
        SET @found_test_sep = CHARINDEX('-', @tests_name);
        SET @found_test = SUBSTRING(@tests_name, 1, @found_test_sep - 1);
        -- isolate test

        PRINT('DEBUG: found_test = ' + @found_test);
        EXEC run_test @found_test, @tests_tests_id, @tests_testruns_id, @found_test_pos;

        -- move to next target
        SET @tests_name = SUBSTRING(@tests_name, @found_test_sep + 1, LEN(@tests_name) - @found_test_sep)
        SET @found_test_pos = @found_test_pos + 1;
    END

    -- execute last token
    SET @found_test = SUBSTRING(@tests_name, 1, LEN(@tests_name));
    IF LEN(@found_test) > 0
    BEGIN
        PRINT('DEBUG: found_test = ' + @found_test);
        EXEC run_test @found_test, @tests_tests_id, @tests_testruns_id, @found_test_pos;
    END

    -- stop timer
    SET @tests_stop = SYSDATETIME();
    UPDATE TestRuns SET EndAt = @tests_stop WHERE TestRunID = @tests_testruns_id;
    SET NOCOUNT OFF;
GO

EXEC run_tests '20*users-40*emails-view_users_all';
-- EXEC run_tests 'view_projects_stars-40*emails-10*users_star_projects';
