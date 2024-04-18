create table if not exists professor (
    id INT PRIMARY KEY  AUTO_INCREMENT ,
    name VARCHAR(255),
    department VARCHAR(255)
);

create table if not exists student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255)
	
);

CREATE TABLE if not exists course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    credits VARCHAR(255),
    professorId INT,
    FOREIGN KEY (professorId) REFERENCES professor(id)
);

CREATE TABLE if not exists course_student (
    courseId INT,
    studentId INT,
    PRIMARY KEY (courseId, studentId),
    FOREIGN KEY (courseId) REFERENCES course(id),
    FOREIGN KEY (studentId) REFERENCES student(id)
);
