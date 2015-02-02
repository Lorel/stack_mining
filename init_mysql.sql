CREATE DATABASE IF NOT EXISTS stackoverflow;

CREATE TABLE IF NOT EXISTS stackoverflow.post (
	id			INTEGER			PRIMARY KEY,
	title           	VARCHAR(256)		NOT NULL,
	body            	TEXT			NOT NULL,
	accepted_answer_id	INTEGER,
	creation_date		TIMESTAMP		NOT NULL,
	score			INTEGER
);

CREATE TABLE IF NOT EXISTS stackoverflow.post_answer (
	id				INTEGER			PRIMARY KEY,
	post_id				INTEGER			NOT NULL,
	answer_id			INTEGER			NOT NULL,
	score				INTEGER			NOT NULL
);

CREATE TABLE IF NOT EXISTS stackoverflow.stack (
	id				INTEGER			AUTO_INCREMENT PRIMARY KEY,
	exception_type			VARCHAR(256)	NOT NULL,
	language			VARCHAR(256)	NOT NULL
);

CREATE TABLE IF NOT EXISTS stackoverflow.post_stack (
	id				INTEGER			AUTO_INCREMENT PRIMARY KEY,
	post_id				INTEGER			NOT NULL,
	stack_id			INTEGER			NOT NULL,
	position			INTEGER			NOT NULL,
	FOREIGN KEY(post_id)  REFERENCES stackoverflow.post(id),
	FOREIGN KEY(stack_id) REFERENCES stackoverflow.stack(id)
);

CREATE TABLE IF NOT EXISTS stackoverflow.frame (
	id		INTEGER 	AUTO_INCREMENT PRIMARY KEY,
	method_name	VARCHAR(256)	NOT NULL,
	file_name	VARCHAR(256)	NOT NULL,
	line_number	INTEGER		NOT NULL,
	UNIQUE frame_unicity (method_name, file_name, line_number)
);

CREATE TABLE IF NOT EXISTS stackoverflow.link (
	id			INTEGER 	AUTO_INCREMENT PRIMARY KEY,
	parent_frame_id		INTEGER		NOT NULL,
	child_frame_id		INTEGER		NOT NULL,
	next_id			INTEGER		NULL,
	UNIQUE link_unicity (parent_frame_id, child_frame_id, next_id),
	FOREIGN KEY(parent_frame_id) REFERENCES stackoverflow.frame(id),
	FOREIGN KEY(child_frame_id) REFERENCES stackoverflow.frame(id),
	FOREIGN KEY(next_id) REFERENCES stackoverflow.link(id)
);

CREATE TABLE IF NOT EXISTS stackoverflow.stack_link (
	id		INTEGER		AUTO_INCREMENT PRIMARY KEY,
	stack_id	INTEGER		NOT NULL,
	link_id		INTEGER		NOT NULL,
	FOREIGN KEY(stack_id)  REFERENCES stackoverflow.stack(id),
	FOREIGN KEY(link_id) REFERENCES stackoverflow.link(id)
);

GRANT ALL ON stackoverflow.* TO 'stackoverflow'@'%' IDENTIFIED BY 'stackoverflow' WITH GRANT OPTION;
