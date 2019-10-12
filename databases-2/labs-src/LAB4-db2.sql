ALTER PROCEDURE insert_data_in_tables AS
	BEGIN TRY
		BEGIN TRANSACTION
		INSERT INTO [Utilizator]
		VALUES('user_lab4','password','lab4@gmail.com','Romania')
		
		SELECT * FROM Utilizator
		RAISERROR ('Error raised in TRY block.',16,1);
		
		INSERT INTO [VizualizareMovie]
		VALUES(1,1,NULL)
		COMMIT TRANSACTION
	END TRY
	BEGIN CATCH
		ROLLBACK TRANSACTION
		PRINT 'Transaction rolled back.'
		SELECT * FROM Utilizator
	END CATCH

EXEC insert_data_in_tables

	--part2
	
ALTER PROCEDURE savepoint_procedure AS
	BEGIN TRY
		BEGIN TRANSACTION
			DECLARE @TranCounter INT;
			SET @TranCounter = @@TRANCOUNT;
			INSERT INTO Actor
			VALUES ('Dustin Hoffman','1937-08-08')
			SAVE TRANSACTION actor_inserted
			RAISERROR ('Error raised in TRY block.',16,1);
			DECLARE @id_dustin_hoffman int
			SET @id_dustin_hoffman=(SELECT id FROM Actor Where name = 'Christopher Plummer')
			INSERT INTO MovieRole
			VALUES(@id_dustin_hoffman,9,'Ted Kramer')
			
		COMMIT TRANSACTION
	END TRY
		
	
	BEGIN CATCH
		SELECT * FROM Actor
		IF @TranCounter > 0
		BEGIN
			ROLLBACK TRANSACTION actor_inserted
			COMMIT TRANSACTION
			PRINT 'Transaction actor_inserted rolled back.'
		END
		ELSE
		BEGIN
			ROLLBACK TRANSACTION
			PRINT 'Transaction rolled back.'
			
		END
		SELECT * FROM Actor
	END CATCH
	

EXEC savepoint_procedure
	
	--part3
	
--non-repeatable read

ALTER PROCEDURE non_repeatable_read_1 AS
BEGIN

	SET TRANSACTION ISOLATION LEVEL READ COMMITTED
	
	BEGIN TRANSACTION
		
		Select * from Utilizator
		where country = 'Romania'

		WAITFOR DELAY '0:0:03'
		
		Select * from Utilizator
		where country = 'Romania'
		
	COMMIT TRANSACTION
END

ALTER PROCEDURE non_repeatable_read_2 AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ COMMITTED
	
	BEGIN TRANSACTION

		UPDATE Utilizator set email = 'g@gmail.com' where country = 'Romania'
		
	COMMIT TRANSACTION
END

EXEC non_repeatable_read_1

--dirty read
CREATE PROCEDURE dirty_read_1 AS
BEGIN

	SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
	
	BEGIN TRANSACTION
		
		Select * from Utilizator
		where country = 'Romania'

		WAITFOR DELAY '0:0:03'
		
		Select * from Utilizator
		where country = 'Romania'
		
		WAITFOR DELAY '0:0:03'
		
		Select * from Utilizator
		where country = 'Romania'
	COMMIT TRANSACTION
END

CREATE PROCEDURE dirty_read_2 AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
	
	BEGIN TRANSACTION

		UPDATE Utilizator set email = 'g@gmail.com' where country = 'Romania'
		
		WAITFOR DELAY '0:0:03'
		
	ROLLBACK TRANSACTION
END

	
EXEC dirty_read_1

--phantom read

CREATE PROCEDURE phantom_read_1 AS
BEGIN

	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ 
	
	BEGIN TRANSACTION
		
		Select * from Utilizator
		where country = 'Romania'

		WAITFOR DELAY '0:0:03'
		
		Select * from Utilizator
		where country = 'Romania'
		
	COMMIT TRANSACTION
END

CREATE PROCEDURE phantom_read_2 AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
	
	BEGIN TRANSACTION

		INSERT INTO Utilizator values('phantom','phantom','phantom','Romania')
		
	COMMIT TRANSACTION
END
	
EXEC dirty_read_1

EXEC deadlock_1
	
	SELECT * FROM Utilizator
	DELETE FROM Utilizator
	WHERE id>6
	SELECT * FROM Movie
	
	DELETE FROM VizualizareMovie
	WHERE id>20000
	
	
	SELECT * FROM MOVIE
	SELECT * FROM MOVIEROLE
	DELETE FROM MOVIEROLE
	WHERE name='Ted Kramer'
	SELECT * FROM ACTOR
	DELETE FROM ACTOR
	WHERE id>6