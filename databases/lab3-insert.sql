-- Daniel Butum - G921
USE leyyin;

-- insert data
SET NOCOUNT ON;
DELETE FROM snippets;
DELETE FROM labels_links;
DELETE FROM labels;
DELETE FROM issue_comments;
DELETE FROM issues;
DELETE FROM milestones;
DELETE FROM users_star_projects;
DELETE FROM users_projects;
DELETE FROM projects;
DELETE FROM emails;
DELETE FROM users;
DBCC CHECKIDENT('users', RESEED, 0);
DBCC CHECKIDENT('emails', RESEED, 0);
DBCC CHECKIDENT('projects', RESEED, 0);
DBCC CHECKIDENT('issues', RESEED, 0);
DBCC CHECKIDENT('milestones', RESEED, 0);
DBCC CHECKIDENT('labels', RESEED, 0);
SET NOCOUNT OFF;

INSERT INTO users(username, password_hash, name, created_at, updated_at)
	VALUES
	('mitchellburkewPT', 'Q3WY3mahwW6j6yu', 'Mitchell Burke', '1992-01-04', '1992-01-04'),
	('jazminstephensSHs', 'NAqXh8pDVHqx4Cp', 'Jazmin Stephens', '1981-03-27', '1981-03-27'),
	('brookssanfordVUf', 'Ghfa5pXZRXgWsFH', 'Brooks Sanford', '1951-01-03', '1951-01-03'),
	('kellanboyerHgd', 'UHw676XxcA9NtWV', 'Kellan Boyer', '1984-07-07', '1984-07-07'),
	('dylandiazC3p', 'adzg7cHGdvgAgrK', 'Dylan Diaz', '1994-07-07', '1994-07-07'),
	('dixietorresBP3', 'wm7VX7H5fYYuxZu', 'Dixie Torres', '1962-06-10', '1962-06-10'),
	('larissamcintoshQuT', '7nY9QfYUQwdH5qv', 'Larissa Mcintosh', '1963-05-04', '1963-05-04'),
	('aliaforbes2YM', 'c95r4VK3F6QTD9t', 'Alia Forbes', '1963-05-24', '1963-05-24'),
	('genevieveblanchardUA8', 'XqJk5dwNV34eqCu', 'Genevieve Blanchard', '1962-08-31', '1962-08-31'),
	('jadadodson2zv', 'uJDzGZd66KJCnd9', 'Jada Dodson', '1987-04-04', '1987-04-04'),
	('aylabarrerabsF', 'v4mUUbKAsNp64ub', 'Ayla Barrera', '1962-04-04', '1962-04-04'),
	('skyemillsbnc', 'dCba3eq5YY6pHSS', 'Skye Mills', '1958-02-10', '1958-02-10'),
	('sashareynolds3kQ', 'YJFJ4n79tVgPHsn', 'Sasha Reynolds', '1990-10-16', '1990-10-16'),
	('ceciliahicksRgZ', 'AQPSXw46dnhTguC', 'Cecilia Hicks', '1967-02-02', '1967-02-02'),
	('pearlhobbswCz', 'jQGPGYbVnNPfuvz', 'Pearl Hobbs', '1952-01-25', '1952-01-25'),
	('alisonfosterRdU', '5j3KdCxR43yk6uN', 'Alison Foster', '1960-11-09', '1960-11-09'),
	('islaboltonq9W', 'BXZ2p2dpcuz5QFy', 'Isla Bolton', '1977-12-17', '1977-12-17'),
	('madalynchenPCF', 'Sv9Ba4w3cn87SyP', 'Madalyn Chen', '1993-02-01', '1993-02-01'),
	('aidanwintersa8k', '23r25DEKhUFJGxW', 'Aidan Winters', '1984-04-17', '1984-04-17'),
	('remingtonmccallxJp', 'Z5XmeeuHm9dVDvA', 'Remington Mccall', '1972-01-01', '1972-01-01'),
	('korbinbonner8UR', 'qKH66WHY3z6S8h3', 'Korbin Bonner', '1967-10-27', '1967-10-27')

INSERT INTO emails(user_id, email)
	VALUES
    ('1', 'mitchellburkewPT@example.com'), 
    ('2', 'jazminstephensSHs@example.com'), 
	('2', 'jazminstephensSHs@gmail.com.com'), 
    ('3', 'brookssanfordVUf@example.com'), 
    ('4', 'kellanboyerHgd@example.com'),
	('4', 'kellanboyerHgd@ubblcluj.ro'), 
    ('5', 'dylandiazC3p@example.com'), 
    ('6', 'dixietorresBP3@example.com'), 
    ('7', 'larissamcintoshQuT@example.com'), 
    ('8', 'aliaforbes2YM@example.com'), 
    ('9', 'genevieveblanchardUA8@example.com'), 
    ('10', 'jadadodson2zv@example.com'),
	('10', 'jadadodson2zv@hotmail.com'),
	('10', 'jadadodson2zv@yahoo.com'),   
    ('11', 'aylabarrerabsF@example.com'), 
    ('12', 'skyemillsbnc@example.com'), 
    ('13', 'sashareynolds3kQ@example.com'), 
    ('14', 'ceciliahicksRgZ@example.com'), 
	('14', 'ceciliahicksRgZ@yahoo.com'),
    ('15', 'pearlhobbswCz@example.com'), 
    ('16', 'alisonfosterRdU@example.com'),
	('16', 'alisonfosterRdU@gmail.com'),  
    ('17', 'islaboltonq9W@example.com'), 
    ('18', 'madalynchenPCF@example.com'), 
	('18', 'madalynchenPCF@yahoo.com'), 
    ('19', 'aidanwintersa8k@example.com'), 
    ('20', 'remingtonmccallxJp@example.com'), 
    ('21', 'korbinbonner8UR@example.com');

INSERT INTO projects(creator_id, name, [description], [path], issues_enabled)
	VALUES
	('1', 'godot', 'Godot Game Engine', 'godot', '1'),
    ('2', 'stk-code', 'The code base of supertuxkart', 'stk-code', '1'),
    ('3', 'stkaddons', 'SuperTuxKart Addons Website', 'stkaddons', '1'),
    ('4', 'chromeos-apk', 'Run Android APKs in Chrome OS OR Chrome in OS X, Linux and Windows.', 'chromeos-apk', '0'),
    ('5', 'Polycode', 'Polycode is a cross-platform framework for creative code.', 'Polycode', '1');

INSERT INTO users_projects(project_id, user_id, project_access)
	VALUES
	('1', '1', '0'),

    ('2', '7',  '0'),
	('2', '11', '0'),
	('2', '19', '1'),

    ('3', '13', '0'),
    ('4', '18', '0'), 
	('4', '21', '1'), 
    ('5', '21', '0')

INSERT INTO users_star_projects(user_id, project_id)
	VALUES
	('1', '1'),
	('2', '1'),
	('5', '1'),
	('7', '1'),
	('20', '1'),

	('7', '2'),
	('20', '2'),

	('2', '3'),
	('12', '3'),
	('13', '3'),
	
	('7', '4'),
	('18', '4'),

	('20', '5'),
	('5', '5'),
	('7', '5'),
	('13', '5')

INSERT INTO milestones(project_id, title, due_at)
	VALUES
	('1', 'beta-release', '2967-10-27'), 
	('2', '0.8.2', '2967-10-27'), 
	('2', '0.8.3', '2967-10-27'),
	('2', '0.8.4', '2967-10-27'),
	('3', '2.0 tuxnet', '2967-10-27'),
	('4', 'alpha', '2967-10-27'),
	('5', 'stable 25.0', '2967-10-27'); 

INSERT INTO issues(project_id, author_id, milestone_id, title, [state])
	VALUES
	('1', '3', NULL, 'Requesting a 3D Raycasting Example', '1'),  
	('2', '10', NULL, 'OpenGL errors on Intel HD graphics 2000', '1'), 
	('2', '7', NULL, 'Force texture compression off on intel HD', '1'),
	('2', '2', '2', 'Update Launchpad translations', '1'),
	('2', '18', '2', 'Crash on Intel HD4000 with shadows enabled', '1'),
	('2', '10', '2', 'Crash pressing ESC on login screen', '0'),
	('2', '20', '3', 'Weather effects are not visible when you look backwards', '1'),
	('2', '21', '2', 'shader clean up', '1'),
	('3', '13', NULL, 'Addon searching from web ui', '0'),
	('3', '13', NULL, 'Remove use of sql.php from code', '0'),
	('5', '17', NULL, '[CRASH REPORT] 18-11-14', '1'),  
	('5', '5', NULL, 'PolyProject must reside on the same drive as the IDE binary', '1'),  
	('5', '9', NULL, 'IDE crashes when loading this image', '1'); -- 13

INSERT INTO issue_comments(issue_id, author_id, [description], commit_hash)
	VALUES
	('2', '19', 'Something else', '38d968ed7188adb22bff6a680e3c91f2'),
	('2', '19', 'These are the first ~380 lines of the stk log:', '38d968ed7188adb22bff6a680e3c91f2'),
	('2', '1', 'Just note that the stdout is already written to ~/.config/supertuxkart/0.8.2/stdout.log', '38d968ed7188adb22bff6a680e3c91f2'),
	('2', '11', 'I started the game with "Always show login screen" enabled, ', '38d968ed7188adb22bff6a680e3c91f2'),
	('5', '5', 'API should be hidden behind a routing scheme', '38d968ed7188adb22bff6a680e3c91f2'),
	('13', '11', 'I started the game with "Always show login screen" enabled, ', '38d968ed7188adb22bff6a680e3c91f2'),
	('13', '11', 'I started the game with "Always show login screen" enabled, ', '38d968ed7188adb22bff6a680e3c91f2');

INSERT INTO labels(project_id, title, color)
	VALUES
	('1', 'bug', 'red'),
	('1', 'wishlist', 'purple'),
	('1', 'duplicate', 'grey'),
	('2', 'GUI', 'grey'),
	('2', 'Online', 'grey'),
	('2', 'duplicate', 'green'),
	('5', 'IDE', 'yellow'),
	('5', 'Critical', 'red');

INSERT INTO labels_links(issue_id, label_id)
	VALUES
	('4', '5'),
	('4', '4')
