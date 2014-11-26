CREATE DATABASE IF NOT EXISTS stackoverflow;

CREATE TABLE IF NOT EXISTS stackoverflow.post (
	id					INTEGER			PRIMARY KEY,
	title           	VARCHAR(256)	NOT NULL,
	body            	TEXT			NOT NULL,
	accepted_answer_id	INTEGER,
	creation_date		TIMESTAMP		NOT NULL
);

CREATE TABLE IF NOT EXISTS stackoverflow.post_answer (
	id					INTEGER			PRIMARY KEY,
	post_id				INTEGER			NOT NULL,
	answer_id			INTEGER			NOT NULL,
	score				INTEGER			NOT NULL
);

CREATE TABLE IF NOT EXISTS stackoverflow.stack (
	id					INTEGER			PRIMARY KEY,
	language			VARCHAR(256)	NOT NULL
);

CREATE TABLE IF NOT EXISTS stackoverflow.post_stack (
	id					INTEGER			PRIMARY KEY,
	post_id				INTEGER			NOT NULL,
	stack_id			INTEGER			NOT NULL,
	position			INTEGER			NOT NULL
);

CREATE TABLE IF NOT EXISTS stackoverflow.frame (
	id					INTEGER AUTO_INCREMENT PRIMARY KEY,
	method_name			VARCHAR(256)	NOT NULL,
	file_name			VARCHAR(256)	NOT NULL,
	line_number			INTEGER			NOT NULL
);

CREATE TABLE IF NOT EXISTS stackoverflow.link (
	id					INTEGER			PRIMARY KEY,
	parent_frame_id		INTEGER			NOT NULL,
	child_frame_id		INTEGER			NOT NULL,
	next_id				INTEGER			NOT NULL
);

CREATE TABLE IF NOT EXISTS stackoverflow.stack_link (
	id					INTEGER			PRIMARY KEY,
	stack_id			INTEGER			NOT NULL,
	link_id				INTEGER			NOT NULL
);

GRANT ALL ON stackoverflow.* TO 'stackoverflow'@'%' IDENTIFIED BY 'stackoverflow' WITH GRANT OPTION;