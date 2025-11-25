
-- =====================================================
-- TABLES CREATION
-- =====================================================
PRAGMA foreign_keys = ON;

CREATE TABLE documents (
    id INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    nb_copies INTEGER NOT NULL CHECK(nb_copies >= 0)
);

CREATE TABLE magazines (
    magazine_id INTEGER PRIMARY KEY,    
    magazine_title TEXT NOT NULL UNIQUE,               
    periodicity TEXT NOT NULL CHECK (periodicity IN ('Daily', 'Weekly', 'Monthly', 'Quarterly', 'Annually'))
);

CREATE TABLE  magazine_numbers (
    id INTEGER PRIMARY KEY,
    magazine_id INTEGER NOT NULL, 
    issue_number INTEGER NOT NULL,
    issue_date DATE DEFAULT (DATE('now')),
    UNIQUE (magazine_id, issue_number),
    FOREIGN KEY (id) REFERENCES documents(id) ON DELETE CASCADE,
    FOREIGN KEY (magazine_id) REFERENCES magazines(magazine_id) ON DELETE CASCADE
);

CREATE TABLE books (
    id INTEGER PRIMARY KEY,
    isbn TEXT NOT NULL UNIQUE,
    pages_number INTEGER NOT NULL CHECK (pages_number > 0),
    year INTEGER DEFAULT (CAST(STRFTIME('%Y', 'now') AS INTEGER)),
    FOREIGN KEY (id) REFERENCES documents(id) ON DELETE CASCADE
);

CREATE TABLE  documents_genres (
    document_id INTEGER NOT NULL,
    genre TEXT NOT NULL CHECK (genre IN (
        'Fiction','Classics','Romance','Mystery','Thriller','Detective','Adventure','Fantasy','Science fiction',
        'Historical fiction','Drama','Poetry','Philosophy','Psychology','Sociology','Politics','History','Education',
        'Law','Religion','Anthropology','Economy','Finance','Management','Marketing','Entrepreneurship','Accounting',
        'Business strategy','Science','Mathematics','Physics','Chemistry','Biology','Medicine','Computer science',
        'Engineering','Environment','Astronomy','Art','Architecture','Design','Photography','Cinema','Music',
        'Literature','Culture','Travel','Cooking','Fashion','Beauty','Home','Gardening','Sports','Well being',
        'Family','Lifestyle','Children','Teen','Comics','Games')),
    PRIMARY KEY (document_id, genre),
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

CREATE TABLE people (
    id INTEGER PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    birth_date DATE
);

CREATE TABLE  documents_authors (
    document_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    PRIMARY KEY (document_id, author_id),
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES people(id) ON DELETE CASCADE
);

CREATE TABLE  members (
    id INTEGER PRIMARY KEY,
    penalty_status BOOLEAN DEFAULT 0,
    phone_number TEXT,
    address TEXT,
    mail TEXT UNIQUE,
    password TEXT NOT NULL,
    is_library_worker BOOLEAN DEFAULT 0,
    FOREIGN KEY (id) REFERENCES people(id) ON DELETE CASCADE
);

CREATE TABLE  borrowings (
    id INTEGER PRIMARY KEY,
    document_id INTEGER NOT NULL,
    person_id INTEGER NOT NULL,
    start_date DATE DEFAULT (DATE('now')),
    expected_end_date DATE,
    real_end_date DATE,
    fine_paid BOOLEAN DEFAULT 0,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    FOREIGN KEY (person_id) REFERENCES people(id) ON DELETE CASCADE
);

-- =====================================================
-- DATA INSERTION
-- =====================================================

-- People
INSERT INTO people (id, first_name, last_name, birth_date) VALUES
(1, 'George', 'Orwell', '1903-06-25'), -- author
(2, 'Alison', 'George', '1988-01-03'), -- author
(3, 'Isaac', 'Asimov', '1920-01-02'), -- author
(4, 'admin', 'admin', '2000-01-01'), -- library worker
(5, 'Bob', 'Johnson', '1988-09-02'), -- regular member
(6, 'Alice', 'Martin', '1995-04-18'), -- regular member
(7, 'Dean', 'Winchester', '1988-09-02'), -- regular member
(8, 'Rory', 'Gilmore', '1995-04-18'), -- regular member
(9, 'Clara', 'Dupont', '1975-12-10'), -- library worker
(10, 'Marie', 'Curie', '1867-11-07'); -- author

-- Documents
INSERT INTO documents (id, title, nb_copies) VALUES
(1, '1984', 10), -- book
(2, 'Animal Farm', 10), -- book
(3, 'Introduction to Radiochemistry', 15), -- book
(4, 'The Hunger Games', 23), -- book
(5, 'Science Weekly – Issue 152', 12), -- magazine issue
(6, 'Science Weekly – Issue 153', 14), -- magazine issue
(7, 'Science fiction special: The future of a genre', 15), -- magazine issue
(8, 'The science of climate change', 20), -- magazine issue
(9, 'Exploring the Amazon rainforest', 20); -- magazine issue

-- Books
INSERT INTO books (id, isbn, pages_number, year) VALUES
(1, '9780553293357', 255, 1949),
(2, '9780194347533', 178, 1945),
(3, '9670194267533', 136, 1949),
(4, '9780909023528', 374, 2008);

-- Document Genres
INSERT INTO documents_genres (document_id, genre) VALUES
(1, 'Classics'),
(1, 'Politics'),
(1, 'Fiction'),
(2, 'Classics'),
(2, 'Politics'),
(2, 'Fiction'),
(3, 'Science'),
(3, 'Chemistry'),
(3, 'Education'),
(4, 'Fantasy'),
(4, 'Adventure'),
(4, 'Teen'),
(5, 'Science'),
(5, 'Environment'),
(6, 'Science'),
(6, 'Biology'),
(7, 'Science fiction'),
(7, 'Science'),
(8, 'Science'),
(8, 'Environment'),
(9, 'Travel'),
(9, 'Environment'),
(9, 'Science');

-- Documents Authors
INSERT INTO documents_authors (document_id, author_id) VALUES
(1, 1),
(2, 1),
(3, 10),
(4, 3),
(5, 2),
(5, 3),
(6, 2),
(7, 3),
(8, 3),
(8, 2),
(9, 2),
(9, 3); 

-- Magazines
INSERT INTO magazines (magazine_id, magazine_title, periodicity) VALUES
(1, 'New Scientist', 'Weekly'),
(2, 'Time', 'Weekly'),
(3, 'National Geographic', 'Monthly');

-- Magazine Numbers
INSERT INTO magazine_numbers (id, magazine_id, issue_number, issue_date) VALUES
(5, 1, 152, '2018-11-12'),
(6, 1, 153, '2020-04-01'),
(7, 2, 233, '2008-01-08'),
(8, 3, 65, '2010-06-01'),
(9, 3, 34, '2019-09-15');

-- the password is "pass" everywhere
-- Members
INSERT INTO members (id, penalty_status, phone_number, address, mail, password, is_library_worker) VALUES 
(4, 0, '0000000000', 'Admin Street 1', 'admin@library.com', 'd74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1', 1), 
(5, 0, '0611223344', '12 Green Road', 'bob.johnson@mail.com', 'd74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1', 0),
(6, 0, '0622334455', '5 Rose Avenue', 'alice.martin@mail.com', 'd74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1', 0),
(7, 1, '0699887766', '45 Hunter Street', 'dean.winchester@mail.com', 'd74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1', 0),
(8, 0, '0677554433', 'Stars Hollow 12', 'rory.gilmore@mail.com', 'd74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1', 0),
(9, 0, '0610101010', '3 Avenue de Paris', 'clara.dupont@library.com', 'd74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1', 1);

-- Borrowings
INSERT INTO borrowings (id, document_id, person_id, start_date, expected_end_date, real_end_date, fine_paid) VALUES
-------------------------------------------------------------------
-- MEMBER 5 : Bob Johnson (Regular) -> 2 active, 2 past (1 overdue past)
-------------------------------------------------------------------
-- Active (not overdue)
(1, 4, 5, '2025-11-10', '2025-11-24', NULL, 0),
(2, 6, 5, '2025-11-15', '2025-11-29', NULL, 0),
-- Past (returned on time)
(3, 1, 5, '2025-10-01', '2025-10-15', '2025-10-14', 0),
-- Past (overdue, 8 days late -> 4$ fine)
(4, 2, 5, '2025-09-01', '2025-09-15', '2025-09-23', 0),

-------------------------------------------------------------------
-- MEMBER 6 : Alice Martin -> 3 active, 2 past (1 overdue past)
-------------------------------------------------------------------
-- Active (one overdue)
(5, 3, 6, '2025-10-25', '2025-11-08', NULL, 0),  -- Overdue (12 days) -> 6$ fine accumulating
(6, 5, 6, '2025-11-05', '2025-11-19', NULL, 0), 
(7, 7, 6, '2025-11-12', '2025-11-26', NULL, 0),
-- Past, on time
(8, 4, 6, '2025-08-01', '2025-08-15', '2025-08-15', 0),
-- Past, overdue 6 days -> 3$ fine
(9, 1, 6, '2025-06-10', '2025-06-24', '2025-06-30', 0),

-------------------------------------------------------------------
-- MEMBER 7 : Dean Winchester -> BLOCKED (>= 10$ unpaid fine)
-- He has 2 massively overdue active borrowings
-------------------------------------------------------------------
-- Active overdue (35 days late -> 17.5$ fine)
(10, 8, 7, '2025-10-01', '2025-10-15', NULL, 0),
-- Active overdue (50 days late -> 25$ fine)
(11, 9, 7, '2025-09-20', '2025-10-04', NULL, 0),
-- Past, returned very late (20 days late -> 10$)
(12, 3, 7, '2025-07-01', '2025-07-15', '2025-08-04', 0),
-- Past, on time
(13, 2, 7, '2025-03-01', '2025-03-15', '2025-03-14', 0),
-- Past, overdue (10 days late -> 5$ fine)
(14, 6, 7, '2025-04-01', '2025-04-15', '2025-04-25', 0),

-------------------------------------------------------------------
-- MEMBER 8 : Rory Glimore (Regular) -> 2 active, 3 past (all on time)
-------------------------------------------------------------------
-- Active
(15, 1, 8, '2025-11-10', '2025-11-24', NULL, 0),
(16, 7, 8, '2025-11-17', '2025-12-01', NULL, 0),
-- Past (all on time — Rory is a perfect student)
(17, 4, 8, '2025-09-01', '2025-09-15', '2025-09-15', 0),
(18, 3, 8, '2025-05-01', '2025-05-15', '2025-05-15', 0),
(19, 5, 8, '2025-06-10', '2025-06-24', '2025-06-22', 0),

-------------------------------------------------------------------
-- MEMBER 4 : Admin (library worker) -> 1 active, 1 past
-------------------------------------------------------------------
(20, 2, 4, '2025-11-01', '2025-11-15', NULL, 0), -- Active, slightly overdue (5 days late)
(21, 1, 4, '2025-04-01', '2025-04-15', '2025-04-14', 0),

-------------------------------------------------------------------
-- MEMBER 9 : Clara Dupont (library worker) -> 1 active, 1 past overdue
-------------------------------------------------------------------
(22, 8, 9, '2025-11-05', '2025-11-19', NULL, 0), -- Active, on time
(23, 9, 9, '2025-08-01', '2025-08-15', '2025-09-10', 0); -- Past, 26 days late -> 13$ fine