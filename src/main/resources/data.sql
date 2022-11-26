DELETE FROM LIKES;
DELETE FROM FILM_GENRE;
DELETE FROM FRIENDS;
DELETE FROM USERS;
DELETE FROM FILM;
DELETE FROM DIRECTORS;
DELETE FROM FILM_DIRECTOR;
DELETE FROM REVIEWS;
DELETE FROM DIRECTORS;
DELETE FROM FILM_DIRECTOR;

ALTER TABLE LIKES ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILM ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE DIRECTORS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE REVIEWS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE DIRECTORS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE ACTIVITY ALTER COLUMN EVENT_ID RESTART WITH 1;

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
