SET search_path TO studybuddy;

DROP TABLE IF EXISTS reports CASCADE;
DROP TABLE IF EXISTS sessions CASCADE;
DROP TABLE IF EXISTS subjects CASCADE;
DROP TABLE IF EXISTS user_languages CASCADE;
DROP TABLE IF EXISTS tutors CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS administrators CASCADE;

CREATE TABLE administrators (
                                id SERIAL PRIMARY KEY,
                                username VARCHAR(50) UNIQUE NOT NULL,
                                password VARCHAR(100) NOT NULL
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       date_of_birth DATE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       phone VARCHAR(20),
                       password VARCHAR(100) NOT NULL,
                       gender VARCHAR(20),
                       user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('student', 'tutor'))
);

CREATE TABLE students (
                          user_id INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE tutors (
                        user_id INT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                        education_level VARCHAR(100),
                        is_approved BOOLEAN DEFAULT FALSE,
                        approved_by INT REFERENCES administrators(id)
);

CREATE TABLE user_languages (
                                user_id INT REFERENCES users(id) ON DELETE CASCADE,
                                language VARCHAR(50) NOT NULL,
                                PRIMARY KEY(user_id, language)
);

CREATE TABLE subjects (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          level VARCHAR(50),
                          added_by INT REFERENCES administrators(id)
);


SELECT setval(
               pg_get_serial_sequence('studybuddy.subjects', 'id'),
               (SELECT MAX(id) FROM studybuddy.subjects)
       );

CREATE TABLE sessions (
                          id SERIAL PRIMARY KEY,
                          description TEXT,
                          price DECIMAL(10,2) NOT NULL,
                          location VARCHAR(200),
                          start_date TIMESTAMP NOT NULL,
                          end_date TIMESTAMP NOT NULL,
                          tutor_id INT NOT NULL REFERENCES tutors(user_id) ON DELETE CASCADE,
                          student_id INT REFERENCES students(user_id) ON DELETE CASCADE,
                          subject_id INT REFERENCES subjects(id)
);

SELECT setval(
               pg_get_serial_sequence('studybuddy.sessions', 'id'),
               (SELECT MAX(id) FROM studybuddy.sessions)
       );

CREATE TABLE reports (
                         id SERIAL PRIMARY KEY,
                         title VARCHAR(200) NOT NULL,
                         description TEXT,
                         reporter_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                         reviewed_by INT REFERENCES administrators(id)
);

CREATE TABLE subject_requests (
                                  id SERIAL PRIMARY KEY,
                                  tutor_email VARCHAR(255) NOT NULL,
                                  subject_name VARCHAR(255) NOT NULL,
                                  reason TEXT,
                                  status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                                  requested_at TIMESTAMP NOT NULL DEFAULT NOW()
);
