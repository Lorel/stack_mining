CREATE USER 'stackoverflow'@'%' IDENTIFIED BY 'stackoverflow';
CREATE DATABASE stackoverflow;

CREATE TABLE stackoverflow.post (
	id					INTEGER			PRIMARY KEY,
	title           	VARCHAR(256)	NOT NULL,
	body            	TEXT			NOT NULL,
	accepted_answer_id	INTEGER,
	creation_date		TIMESTAMP		NOT NULL
);

CREATE TABLE stackoverflow.post_answer (
	id					INTEGER			PRIMARY KEY,
	post_id				INTEGER			NOT NULL,
	answer_id			INTEGER			NOT NULL,
	score				INTEGER			NOT NULL
);

CREATE TABLE stackoverflow.stack (
	id					INTEGER			PRIMARY KEY,
	language			VARCHAR(256)	NOT NULL
);

CREATE TABLE stackoverflow.post_answer (
	id					INTEGER			PRIMARY KEY,
	post_id				INTEGER			NOT NULL,
	stack_id			INTEGER			NOT NULL,
	position			INTEGER			NOT NULL
);

CREATE TABLE stackoverflow.frame (
	id					INTEGER			PRIMARY KEY,
	method_name			VARCHAR(256)	NOT NULL,
	file_name			VARCHAR(256)	NOT NULL,
	line_number			INTEGER			NOT NULL
);

CREATE TABLE stackoverflow.link (
	id					INTEGER			PRIMARY KEY,
	parent_frame_id		INTEGER			NOT NULL,
	child_frame_id		INTEGER			NOT NULL,
	next_id				INTEGER			NOT NULL
);

CREATE TABLE stackoverflow.stack_link (
	id					INTEGER			PRIMARY KEY,
	stack_id			INTEGER			NOT NULL,
	link	_id			INTEGER			NOT NULL
);

GRANT ALL ON stackoverflow.* TO 'stackoverflow'@'%' WITH GRANT OPTION;