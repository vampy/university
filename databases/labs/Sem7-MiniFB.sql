USE test_example_dbms
GO

DROP TABLE Comments
DROP TABLE Posts
DROP TABLE Likes
DROP TABLE Pages
DROP TABLE Categories
DROP TABLE Users
GO

CREATE TABLE Users
    (uid INT PRIMARY KEY IDENTITY(1,1),
     name VARCHAR(100),
     city VARCHAR(100),
     age TINYINT)

CREATE TABLE Categories
    (cid SMALLINT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(50),
    description VARCHAR(100))

CREATE TABLE Pages
    (pid INT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(100),
    cid SMALLINT REFERENCES Categories(cid))

CREATE TABLE Likes
    (uid INT REFERENCES Users(uid),
    pid INT REFERENCES Pages(pid),
    date DATETIME,
    PRIMARY KEY(uid,pid))

CREATE TABLE Posts
    (postid INT PRIMARY KEY IDENTITY(1,1),
    date DATETIME,
    text VARCHAR(1000),
    shares INT,
    uid INT REFERENCES Users(uid))

CREATE TABLE Comments
    (commid INT PRIMARY KEY IDENTITY(1,1),
    text VARCHAR(1000),
    date DATETIME,
    topcomm BIT,
    postid INT REFERENCES Posts(postid) ON DELETE CASCADE ON UPDATE CASCADE)
GO

--INSERTS
-----------------------
INSERT Users(name, city, age) VALUES ('u1','c',18) --ok
INSERT Users(name, city, age) VALUES ('u2','c',18)
INSERT Users(name, city, age) VALUES ('u3','c',20)
INSERT Users(name, city, age) VALUES ('u4','c',24) --ok
INSERT Users(name, city, age) VALUES ('u5','c',13)

INSERT Categories(name, description) VALUES ('cat1','cat1desc')
INSERT Categories(name, description) VALUES ('cat2','cat2desc')

INSERT Pages(name,cid) VALUES('p1',1)
INSERT Pages(name,cid) VALUES('p2',1)
INSERT Pages(name,cid) VALUES('p3',1)
INSERT Pages(name,cid) VALUES('p4',1)
INSERT Pages(name,cid) VALUES('p5',1)
INSERT Pages(name,cid) VALUES('p6',1)
INSERT Pages(name,cid) VALUES('p7',2)

INSERT Likes(uid, pid, date) VALUES(1,1, '1-1-2015')
INSERT Likes(uid, pid, date) VALUES(1,2, '1-1-2015')
INSERT Likes(uid, pid, date) VALUES(1,3, '1-1-2015')
INSERT Likes(uid, pid, date) VALUES(2,1, '1-1-2015')
INSERT Likes(uid, pid, date) VALUES(5,1, '1-1-2015')

INSERT Posts(date, shares, text, uid) VALUES ('5-10-2015',1,'po1',1)

INSERT Comments(text, date, topcomm, postid) VALUES ('co1','1-1-2015',1,1)
INSERT Comments(text, date, topcomm, postid) VALUES ('co2','1-1-2015',0,1)

INSERT Posts(date, shares, text, uid) VALUES ('5-10-2015',1,'po2',2)
INSERT Comments(text, date, topcomm, postid) VALUES ('co3','1-1-2015',1,2)


INSERT Posts(date, shares, text, uid) VALUES ('5-10-2015',1,'po3',4)
INSERT Comments(text, date, topcomm, postid) VALUES ('co4','1-1-2015',1,3)
INSERT Comments(text, date, topcomm, postid) VALUES ('co5','1-1-2015',0,3)


--3--
SELECT u.name, u.age, ISNULL(noOfLikes,0)
FROM Users u INNER JOIN
    (
        SELECT u.uid, c.postid, COUNT(commid) AS noOfComments
        FROM Users u INNER JOIN Posts p ON u.uid = p.uid
            INNER JOIN Comments c ON p.postid = c.postid
        WHERE u.age BETWEEN 18 AND 24 AND p.date BETWEEN '5-6-2015' AND '6-5-2015'
        GROUP BY u.uid, c.postid
        HAVING COUNT(commid) >=2
    ) t1
    ON u.uid = t1.uid
    LEFT JOIN
    (
        SELECT uid, COUNT(pid) noOfLikes
        FROM Likes
        GROUP BY uid
    ) t2
    ON t1.uid = t2.uid

ORDER BY noOfLikes ASC

GO
IF EXISTS (SELECT name FROM sys.indexes WHERE name = N'idx_nc_comments_postid')
    DROP INDEX idx_nc_comments_postid ON Comments;

CREATE NONCLUSTERED INDEX idx_nc_comments_postid
ON Comments(postid)

GO

SELECT *
FROM Comments
WITH (INDEX(idx_nc_comments_postid))
where postid = 3

SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
BEGIN TRAN

    SELECT * FROM Users WHERE uid = 1
    WAITFOR DELAY '00:00:03'
    SELECT * FROM Users WHERE uid =1
COMMIT TRAN
