USE test;

DECLARE @id INT;
DECLARE @upgrade_proc VARCHAR(255);
DECLARE @revert_proc VARCHAR(255);

--DECLARE @sp_name NVARCHAR(255);
--SET @sp_name = 'undo_delete_milestones_description';
--EXEC sp_executesql @sp_name;

DECLARE cursor_names CURSOR FOR SELECT
                                    id,
                                    [upgrade_proc],
                                    [revert_proc]
                                FROM [schema_migrations];

OPEN cursor_names;

FETCH NEXT FROM cursor_names
INTO @id, @upgrade_proc, @revert_proc;
WHILE @@FETCH_STATUS = 0
    BEGIN
        PRINT CONVERT(CHAR(2), @id) + '	' + @upgrade_proc;
        FETCH NEXT FROM cursor_names
        INTO @id, @upgrade_proc, @revert_proc;
    END

CLOSE cursor_names;
DEALLOCATE cursor_names;
