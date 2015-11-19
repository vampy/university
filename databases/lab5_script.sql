USE test;
GO

IF OBJECT_ID('dbo.Movie', 'U') IS NOT NULL
DROP TABLE dbo.Movie


CREATE TABLE Movie

(MovieID INT PRIMARY KEY IDENTITY(1,1),

Title NVARCHAR(100),

Director NVARCHAR(100),

YearOfRelease SMALLINT,

Nominations SMALLINT)

GO

DELETE FROM Movie
GO

DBCC CHECKIDENT('Movie', RESEED, 0) 

INSERT INTO Movie (Title, Director, YearOfRelease) 

SELECT 'the hobbit: the desolation of smaug', 'jackson', 2013

UNION ALL

SELECT 'a fost sau n-a fost?', 'porumboiu', 2006

UNION ALL

SELECT 'close encounters of the 3rd kind', 'spielberg', 1977

UNION ALL

SELECT 'madagascar 3: europe''s most wanted', 'darnell', 2012

UNION ALL

SELECT 'moscow does not believe in tears', 'menshov', 1979

UNION ALL

SELECT 'contact', 'zemeckis', 1997

UNION ALL

SELECT 'dupa dealuri', 'mungiu', 2012

GO

SELECT * FROM Movie
GO

DECLARE @i INT
SET @i = 0
WHILE @i < 50000
BEGIN
  INSERT INTO Movie (Title, Director, YearOfRelease) VALUES ('title' + CAST(@i AS VARCHAR(5)), 'director' + CAST(@i AS VARCHAR(5)), 
    CAST((1900 + (@i / 500)) AS VARCHAR(4)))
  SET @i = @i + 1
END

GO

SELECT COUNT(*) FROM Movie

CREATE NONCLUSTERED INDEX idx_nc_director
	ON Movie(Director);

UPDATE Movie SET Director = 'whatever' WHERE MovieID = 34;

SELECT * FROM Movie 
WHERE [Director] = 'spielberg'


SELECT OBJECT_NAME(i.object_id) table_name, i.index_id,
  i.name, i.type, i.type_desc, i.is_unique, i.is_primary_key,
  ic.index_column_id, ic.column_id,  c.name, ic.is_included_column
 FROM sys.indexes i INNER JOIN sys.index_columns ic 
   ON i.object_id = ic.object_id AND i.index_id = ic.index_id  
   INNER JOIN sys.columns c
   ON ic.object_id = c.object_id AND ic.column_id = c.column_id
   INNER JOIN sys.objects o ON c.object_id = o.object_id
 WHERE o.type = 'U'

SELECT OBJECT_NAME(s.object_id) table_name, s.index_id,
  i.name, i.type, i.type_desc, i.is_unique, i.is_primary_key,
  ic.index_column_id, ic.column_id,  c.name, ic.is_included_column, 
  s.user_seeks, s.last_user_seek,
  s.user_scans, s.last_user_scan,
  s.user_lookups, s.last_user_lookup,
  s.user_updates, s.last_user_update  
 FROM sys.dm_db_index_usage_stats s INNER JOIN sys.indexes i 
   ON s.object_id = i.object_id AND s.index_id = i.index_id
   INNER JOIN sys.index_columns ic 
   ON i.object_id = ic.object_id AND i.index_id = ic.index_id  
   INNER JOIN sys.columns c
   ON ic.object_id = c.object_id AND ic.column_id = c.column_id
WHERE s.database_id = db_id('test') 



SELECT OBJECT_NAME(s.object_id) table_name,
  i.name, i.type_desc, i.is_unique, i.is_primary_key,
  ic.index_column_id, ic.column_id,  c.name, ic.is_included_column, 
  s.user_seeks, s.last_user_seek,
  s.user_scans, s.last_user_scan,
  s.user_lookups, s.last_user_lookup,
  s.user_updates, s.last_user_update  
 FROM sys.dm_db_index_usage_stats s INNER JOIN sys.indexes i 
   ON s.object_id = i.object_id AND s.index_id = i.index_id
   INNER JOIN sys.index_columns ic 
   ON i.object_id = ic.object_id AND i.index_id = ic.index_id  
   INNER JOIN sys.columns c
   ON ic.object_id = c.object_id AND ic.column_id = c.column_id
WHERE s.database_id = db_id('test') 

SELECT   OBJECT_NAME(S.[OBJECT_ID]) AS [OBJECT NAME], 
         I.[NAME] AS [INDEX NAME], 
         USER_SEEKS, 
         USER_SCANS, 
         USER_LOOKUPS, 
         USER_UPDATES 
FROM     SYS.DM_DB_INDEX_USAGE_STATS AS S 
         INNER JOIN SYS.INDEXES AS I 
         ON I.[OBJECT_ID] = S.[OBJECT_ID] 
            AND I.INDEX_ID = S.INDEX_ID 
WHERE    OBJECTPROPERTY(S.[OBJECT_ID],'IsUserTable') = 1