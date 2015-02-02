DROP DATABASE IF EXISTS stackoverflow;
CREATE DATABASE stackoverflow;

DROP ROLE IF EXISTS stackoverflow;
CREATE USER stackoverflow LOGIN PASSWORD 'stackoverflow';

\connect stackoverflow;

CREATE SCHEMA stackoverflow;

CREATE TABLE stackoverflow.post (
	id			INTEGER			PRIMARY KEY,
	title           	VARCHAR(256)		NOT NULL,
	body            	TEXT			NOT NULL,
	accepted_answer_id	INTEGER,
	creation_date		TIMESTAMP		NOT NULL,
	score			INTEGER
);

CREATE TABLE stackoverflow.stack (
	id				SERIAL 		PRIMARY KEY,
	language			VARCHAR(256)	NOT NULL,
	exception_type			VARCHAR(256)
);

CREATE TABLE stackoverflow.post_stack (
	id				SERIAL			PRIMARY KEY,
	post_id				INTEGER			NOT NULL,
	stack_id			INTEGER			NOT NULL,
	position			INTEGER			NOT NULL,
	FOREIGN KEY(post_id)  REFERENCES stackoverflow.post(id),
	FOREIGN KEY(stack_id) REFERENCES stackoverflow.stack(id)
);

CREATE TABLE stackoverflow.frame (
	id		SERIAL		 PRIMARY KEY,
	method_name	VARCHAR(256)	NOT NULL,
	file_name	VARCHAR(256)	NOT NULL,
	line_number	INTEGER		NOT NULL,
	CONSTRAINT frame_unicity UNIQUE(method_name, file_name, line_number)
);

CREATE TABLE stackoverflow.link (
	id			SERIAL 		PRIMARY KEY,
	parent_frame_id		INTEGER		NOT NULL,
	child_frame_id		INTEGER		NOT NULL,
	next_id			INTEGER		NULL,
	CONSTRAINT link_unicity UNIQUE(parent_frame_id, child_frame_id, next_id),
	FOREIGN KEY(parent_frame_id) REFERENCES stackoverflow.frame(id),
	FOREIGN KEY(child_frame_id) REFERENCES stackoverflow.frame(id),
	FOREIGN KEY(next_id) REFERENCES stackoverflow.link(id)
);

CREATE TABLE stackoverflow.stack_link (
	id		SERIAL		PRIMARY KEY,
	stack_id	INTEGER		NOT NULL,
	link_id		INTEGER		NOT NULL,
	FOREIGN KEY(stack_id)  REFERENCES stackoverflow.stack(id),
	FOREIGN KEY(link_id) REFERENCES stackoverflow.link(id)
);

GRANT ALL ON SCHEMA  stackoverflow TO stackoverflow;
GRANT ALL ON ALL TABLES IN SCHEMA  stackoverflow TO stackoverflow;
GRANT ALL ON ALL SEQUENCES IN SCHEMA  stackoverflow TO stackoverflow;
