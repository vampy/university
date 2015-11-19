-- Daniel Butum - G921

CREATE TABLE users(
	id				INT				NOT NULL PRIMARY KEY IDENTITY(1, 1),

	username		VARCHAR(255)	NOT NULL,
	password_hash	VARCHAR(255)	NOT NULL,
	name			VARCHAR(255)	NULL,

	created_at		DATETIME		NULL,
	updated_at		DATETIME        NULL,
);

CREATE TABLE emails(
	id			INT				NOT NULL PRIMARY KEY IDENTITY(1, 1),
	user_id		INT				NOT NULL,
	 
	email		VARCHAR(255)	NOT NULL,
	created_at	DATETIME		NULL,
	updated_at	DATETIME        NULL,

	CONSTRAINT	FK_emails_user_id FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE projects(
	id					INT				NOT NULL PRIMARY KEY IDENTITY(1, 1),
	creator_id			INT				NOT NULL,

	name				VARCHAR(255)	NOT NULL,
	[path]				VARCHAR(255)    NOT NULL,
	[description]		VARCHAR(255)	NULL,
	issues_enabled		BIT				NOT NULL DEFAULT '1', -- bool field

	created_at			DATETIME		NULL,
	updated_at			DATETIME        NULL,
	last_activity_at	DATETIME		NULL,

	CONSTRAINT	FK_projects_creator_id FOREIGN KEY(creator_id) REFERENCES users(id)
);

-- N-N A project may have different users with different permissions(besides the creator of the project)
CREATE TABLE users_projects(
	user_id			INT				NOT NULL,
	project_id		INT				NOT NULL,
		
	project_access  INT				NOT NULL DEFAULT '0', -- 0 is owner, 1 is collaborator
	created_at		DATETIME		NULL,
	updated_at		DATETIME        NULL,

	CONSTRAINT PK_users_projects			 PRIMARY KEY(user_id, project_id),
	CONSTRAINT FK_users_projects_user_id	 FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT FK_users_projects_project_id  FOREIGN KEY(project_id) REFERENCES projects(id),	
);

-- N-N Hold all the stars for every project
CREATE TABLE users_star_projects(
	user_id			INT				NOT NULL,
	project_id		INT				NOT NULL,	
	created_at		DATETIME		NULL,

	CONSTRAINT PK_users_star_projects			 PRIMARY KEY(user_id, project_id),
	CONSTRAINT FK_users_star_projects_user_id	 FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT FK_users_star_projects_project_id FOREIGN KEY(project_id) REFERENCES projects(id),
);

-- deadline table
CREATE TABLE milestones(
	id				INT				NOT NULL PRIMARY KEY IDENTITY(1, 1),
	project_id		INT				NOT NULL,
	
	title			VARCHAR(255)	NOT NULL,
	[description]	VARCHAR(1024)	NULL,
	[state]			INT				NOT NULL DEFAULT '1', -- 1 is active, 0 is closed

	due_at			DATETIME		NOT NULL, -- deadline date
	created_at		DATETIME		NULL,
	updated_at		DATETIME        NULL,

	CONSTRAINT FK_milestones_project_id	 FOREIGN KEY(project_id) REFERENCES projects(id),
);

-- N - N, project-author
CREATE TABLE issues(
	id				INT				NOT NULL PRIMARY KEY IDENTITY(1, 1),
	author_id		INT				NOT NULL FOREIGN KEY REFERENCES users(id),
	project_id		INT				NOT NULL FOREIGN KEY REFERENCES projects(id),	
	milestone_id	INT				NULL DEFAULT NULL FOREIGN KEY REFERENCES milestones(id), -- optional
	
	title			VARCHAR(128)	NOT NULL,
	[description]	VARCHAR(2048)	NULL,
	branch_name		VARCHAR(255)	NULL DEFAULT 'master',
	[state]			INT				NOT NULL DEFAULT '1', -- 0 is closed, 1 is open, 2 is reopened

	created_at		DATETIME		NULL,
	updated_at		DATETIME        NULL,	
);

CREATE TABLE issue_comments(
	id				INT				NOT NULL PRIMARY KEY IDENTITY(1, 1),
	issue_id		INT				NOT NULL FOREIGN KEY REFERENCES issues(id),
	author_id		INT				NOT NULL FOREIGN KEY REFERENCES users(id),

	[description]	VARCHAR(1024)	NOT NULL,
	line_code		INT				NULL, -- hold the line on which the comment is on
	file_code		VARCHAR(255)	NULL, -- hold the file name
	commit_hash		VARCHAR(255)	NOT NULL, -- hold the commit of the hash

	created_at		DATETIME		NULL,
	updated_at		DATETIME        NULL,
);

-- create specific issues for each project
CREATE TABLE labels(
	id				INT				NOT NULL IDENTITY(1, 1),
	project_id		INT				NOT NULL,
	
	title			VARCHAR(512)	NOT NULL,
	color			VARCHAR(255)	NOt NULL, -- every label has a color

	created_at		DATETIME		NULL,
	updated_at		DATETIME		NULL,

	CONSTRAINT		PK_labels			 PRIMARY KEY(id),
    CONSTRAINT		FK_labels_project_id FOREIGN KEY(project_id) REFERENCES projects(id)
);

-- N-N, every issues has some labels
CREATE TABLE labels_links(
	label_id	INT		NOT NULL,
	issue_id	INT		NOT NULL,

	CONSTRAINT labels_links_pk	PRIMARY KEY(label_id, issue_id),
	CONSTRAINT FK_labels_links_label_id	 FOREIGN KEY(label_id) REFERENCES labels(id),
	CONSTRAINT FK_labels_links_issue_id	 FOREIGN KEY(issue_id) REFERENCES issues(id),
);

-- small code snippets
CREATE TABLE snippets(
	id				INT				NOT NULL PRIMARY KEY IDENTITY(1, 1),
	author_id		INT				NOT NULL,
	project_id		INT				NULL, -- optional

	title			VARCHAR(255)	NOT NULL,
	file_name		VARCHAR(255)	NOT NULL,
	content			TEXT			NOT NULL,
	is_private		BIT				NOT NULL DEFAULT '1', -- bool field

	expires_at		DATETIME		NULL,	-- the date when the snippet will not be accesible anymore
	created_at		DATETIME		NULL,
	updated_at		DATETIME		NULL,

	CONSTRAINT FK_snippets_author_id	 FOREIGN KEY(author_id) REFERENCES users(id),
	CONSTRAINT FK_snippets_project_id	 FOREIGN KEY(project_id) REFERENCES projects(id),
);