
-- =====================================================
-- TABLES CREATION
-- =====================================================
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS documents (
    id INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    nb_copies INTEGER NOT NULL CHECK(nb_copies >= 0)
);

CREATE TABLE IF NOT EXISTS magazines (
    magazine_id INTEGER PRIMARY KEY,    
    magazine_title TEXT NOT NULL UNIQUE,               
    periodicity TEXT NOT NULL CHECK (periodicity IN ('Daily', 'Weekly', 'Monthly', 'Quarterly', 'Annually'))
);

CREATE TABLE IF NOT EXISTS magazine_numbers (
    id INTEGER PRIMARY KEY,
    magazine_id INTEGER NOT NULL, 
    issue_number INTEGER NOT NULL,
    issue_date DATE DEFAULT (DATE('now')),
    UNIQUE (magazine_id, issue_number),
    FOREIGN KEY (id) REFERENCES documents(id) ON DELETE CASCADE,
    FOREIGN KEY (magazine_id) REFERENCES magazines(magazine_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS books (
    id INTEGER PRIMARY KEY,
    isbn TEXT NOT NULL UNIQUE,
    pages_number INTEGER NOT NULL CHECK (pages_number > 0),
    year INTEGER DEFAULT (CAST(STRFTIME('%Y', 'now') AS INTEGER)),
    FOREIGN KEY (id) REFERENCES documents(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS documents_genres (
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

CREATE TABLE IF NOT EXISTS people (
    id INTEGER PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    birth_date DATE
);

CREATE TABLE IF NOT EXISTS documents_authors (
    document_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    PRIMARY KEY (document_id, author_id),
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES people(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS members (
    id INTEGER PRIMARY KEY,
    penalty_status BOOLEAN DEFAULT 0,
    phone_number TEXT,
    address TEXT,
    mail TEXT UNIQUE,
    password TEXT NOT NULL,
    is_library_worker BOOLEAN DEFAULT 0,
    FOREIGN KEY (id) REFERENCES people(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS borrowings (
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
(1, 'George', 'Orwell', '1903-06-25'),
(2, 'Alison', 'George', '1988-01-03'),
(3, 'Isaac', 'Asimov', '1920-01-02'),
(4, 'admin', 'admin', '2000-01-01'),
(5, 'Kate', 'Galkina', '2004-10-10');

-- Documents
INSERT INTO documents (id, title, nb_copies) VALUES
(1, 'Foundation', 1),
(2, 'Animal Farm', 2),
(3, 'The Hunger Games', 3),
(4, 'Science fiction special: The future of a genre', 3),
(5, 'The science of climate change', 5),
(6, 'Exploring the Amazon rainforest', 2);

-- Books
INSERT INTO books (id, isbn, pages_number, year) VALUES
(1, '9780553293357', 255, 1951),
(2, '9780194267533', 176, 1945),
(3, '9780439023528', 374, 2008);

-- Document Genres
INSERT INTO documents_genres (document_id, genre) VALUES
(1, 'Science fiction'),
(2, 'Fiction'),
(3, 'Romance'),
(3, 'Literature'),
(4, 'Science fiction'),
(5, 'Science'),
(6, 'Environment');

-- Documents Authors
INSERT INTO documents_authors (document_id, author_id) VALUES
(1, 3),
(2, 1),
(3, 2),
(4, 2),
(5, 3),
(6, 1);

-- Magazines
INSERT INTO magazines (magazine_id, magazine_title, periodicity) VALUES
(1, 'New Scientist', 'Weekly'),
(2, 'Time', 'Weekly'),
(3, 'National Geographic', 'Monthly');

-- Magazine Numbers
INSERT INTO magazine_numbers (id, magazine_id, issue_number, issue_date) VALUES
(4, 1, 3, '2008-11-12'),
(5, 2, 4, '2020-06-01'),
(6, 3, 5, '2019-09-15');

INSERT INTO members (id, phone_number, address, mail, password, is_library_worker) VALUES 
(4, 'admin', 'admin', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1),
(5, '+330611879072', '121 rue de Bellevue', 'ekaterina.galkina@outlook.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1);