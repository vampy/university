EXEC non_repeatable_read_2

EXEC dirty_read_2

EXEC phantom_read_2


--deadlock

ALTER PROCEDURE deadlock_1 AS
	set XACT_ABORT ON

	DECLARE @Tries	INT,
			@Except BIT
	SET @Tries = 2
	SET @Except = 0
	
Try_Transaction:

	BEGIN TRY
		
		BEGIN TRANSACTION
		--IF @Tries = 1
		--	RAISERROR ('Error raised in TRY block.',16,1);

		UPDATE Movie SET genre = 'drama' WHERE id=1

		WAITFOR DELAY '0:0:05'

		UPDATE Utilizator SET username='deadlock1' WHERE id=6
		
		COMMIT TRANSACTION
		
	END TRY
	
	BEGIN CATCH
		ROLLBACK TRANSACTION
		PRINT '1 : Transaction rolled back.'
		
		SET @Except = 1
				
	END CATCH
	
	IF @Except = 1
	BEGIN
		IF @Tries > 0
		BEGIN
			PRINT '1 : Retry transaction in 3 seconds (' + CONVERT(CHAR(2), @Tries) + '/' + '2)'
			WAITFOR DELAY '0:0:03'
			SET @Except = 0
			SET @Tries = @Tries - 1
			GOTO Try_Transaction
		END
	END
GO

ALTER PROCEDURE deadlock_2

AS
	set XACT_ABORT ON

	DECLARE @Tries	INT,
			@Except BIT
	SET @Tries = 2
	SET @Except = 0
	
Try_Transaction:

	BEGIN TRY
	
		BEGIN TRANSACTION

		UPDATE Utilizator SET username='deadlock2' WHERE id=6

		WAITFOR DELAY '0:0:05'

		UPDATE Movie SET genre = 'drama' WHERE id=1

		COMMIT TRANSACTION
	
	END TRY
	
	BEGIN CATCH
		ROLLBACK TRANSACTION
		PRINT '2 : Transaction rolled back.'
		
		SET @Except = 1
				
	END CATCH
	
	IF @Except = 1
	BEGIN
		IF @Tries > 0
		BEGIN
			PRINT '2 : Retry transaction in 3 seconds (' + CONVERT(CHAR(2), @Tries) + '/ ' + '2)'
			WAITFOR DELAY '0:0:03'
			SET @Except = 0
			SET @Tries = @Tries - 1
			GOTO Try_Transaction
		END
	END
GO

EXEC deadlock_2