DELETE FROM LIKES;
DELETE FROM FILM_GENRE;
DELETE FROM FRIENDS;
DELETE FROM USERS;
DELETE FROM FILM;

ALTER TABLE LIKES ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILM ALTER COLUMN ID RESTART WITH 1;

MERGE INTO MPA
    VALUES ( 1, 'G'),
           ( 2, 'PG'),
           ( 3, 'PG-13'),
           ( 4, 'R'),
           ( 5, 'NC-17');

MERGE INTO GENRE
    VALUES ( 1, 'Комедия' ),
           ( 2, 'Драма' ),
           ( 3, 'Мультфильм' ),
           ( 4, 'Триллер' ),
           ( 5, 'Документальный' ),
           ( 6, 'Боевик' );

INSERT INTO USERS (NAME, LOGIN, EMAIL, BIRTHDAY)
    VALUES ('Алексей', 'alex', 'alex@mail.com', '1991-01-01'),
           ('Егор', 'egor', 'egor@mail.com', '1992-02-02'),
           ('Борис', 'boris', 'boris@mail.com', '1993-03-03'),
           ('Владимир', 'vladimir', 'vladimir@mail.com', '1994-04-04'),
           ('Валентин', 'valentin', 'valentin@mail.com', '1995-05-05'),
           ('Геннадий', 'gena', 'gena@mail.com', '1996-06-06'),
           ('Григорий', 'grig', 'grig@mail.com', '1997-07-07'),
           ('Дмитрий', 'dima', 'dima@mail.com', '1997-07-07'),
           ('Максим', 'max', 'max@mail.com', '1998-08-08'),
           ('Михаил', 'misha', 'misha@mail.com', '1998-09-09');

INSERT INTO FRIENDS (FRIEND_ONE, FRIEND_TWO, CONFIRMED)
    VALUES (1, 2, TRUE),
           (2, 1, TRUE),
           (1, 3, TRUE),
           (3, 1, TRUE),
           (1, 4, TRUE),
           (4, 1, TRUE),
           (2, 3, TRUE),
           (3, 2, TRUE),
           (8, 9, FALSE),
           (8, 10, FALSE);

INSERT INTO FILM (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA)
    VALUES ('Король Лев',  'desc', '1994-10-10', 88, 4, 1),
           ('Аладдин',  'desc', '1992-10-10', 90, 4, 1),
           ('Улика',  'desc', '1985-04-10', 94, 4, 2),
           ('Контакт',  'desc', '1997-04-10', 150, 4, 2),
           ('Начало',  'desc', '2010-11-10', 148, 4, 3),
           ('Престиж',  'desc', '2006-10-10', 125, 4, 3),
           ('Титаник',  'desc', '1997-10-10', 194, 4, 3),
           ('Леон',  'desc', '1994-10-10', 133, 4, 4),
           ('Бойцовский клуб',  'desc', '1999-10-10', 139, 4, 4),
           ('Остров проклятых',  'desc', '2009-10-10', 138, 4, 4),
           ('Святая кровь',  'desc', '1989-10-10', 125, 4, 5),
           ('Трудная мишень',  'desc', '1993-10-10', 122, 4, 5);

INSERT INTO FILM_GENRE (FILM, GENRE)
    VALUES (1, 3),
           (2, 3),
           (3, 1),
           (4, 2),
           (5, 4),
           (5, 5),
           (6, 5),
           (7, 6),
           (7, 1),
           (7, 2),
           (12, 6);

INSERT INTO LIKES (USER_ID, FILM_ID)
    VALUES (1, 1 ),
           (1, 2 ),
           (1, 3 ),
           (2, 4 ),
           (2, 5 ),
           (3, 6 ),
           (4, 7 ),
           (10, 7 ),
           (4, 8 ),
           (5, 9 ),
           (6, 10 ),
           (6, 11 ),
           (1, 11 ),
           (7, 12 ),
           (8, 12 ),
           (1, 12 ),
           (8, 11 ),
           (9, 12 );
