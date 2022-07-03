
--
-- INSERT INTO PUBLIC.MPAA (MPAA_ID,NAME)
-- VALUES (1,'G');
-- INSERT INTO PUBLIC.MPAA (MPAA_ID,NAME)
-- VALUES (2,'PG');
-- INSERT INTO PUBLIC.MPAA (MPAA_ID,NAME)
-- VALUES (3,'PG-13');
-- INSERT INTO PUBLIC.MPAA (MPAA_ID,NAME)
-- VALUES (4,'R');
-- INSERT INTO PUBLIC.MPAA (MPAA_ID,NAME)
-- VALUES (5,'NC-17');
--
-- INSERT INTO PUBLIC.GENRE (GENRE_ID,NAME)
-- VALUES (1,'Комедия');
-- INSERT INTO PUBLIC.GENRE (GENRE_ID,NAME)
-- VALUES (2,'Драма');
-- INSERT INTO PUBLIC.GENRE (GENRE_ID,NAME)
-- VALUES (3,'Мультфильм');
-- INSERT INTO PUBLIC.GENRE (GENRE_ID,NAME)
-- VALUES (4,'Триллер');
-- INSERT INTO PUBLIC.GENRE (GENRE_ID,NAME)
-- VALUES (5,'Документальный');
-- INSERT INTO PUBLIC.GENRE (GENRE_ID,NAME)
-- VALUES (6,'Боевик');
DELETE from LIKES;
delete from GENRE_FILM;
DELETE  FROM  PUBLIC.USERS;
DELETE  FROM  PUBLIC.FRIENDS;
DELETE  FROM  PUBLIC.FILM;

INSERT INTO PUBLIC.USERS (EMAIL,LOGIN,NAME,BIRTHDAY)
VALUES ('user1@mail.ru','user1','user1name','2000-10-10');
INSERT INTO PUBLIC.USERS (EMAIL,LOGIN,NAME,BIRTHDAY)
VALUES ('user2@mail.ru','user2','user2name','1985-05-10');
INSERT INTO PUBLIC.USERS (EMAIL,LOGIN,NAME,BIRTHDAY)
VALUES ('user3@mail.ru','user3','user3name','1990-12-31');

INSERT INTO PUBLIC.FRIENDS (USER_ID,FRIEND_ID,FRIENDSHIP)
VALUES (1,2,true);
INSERT INTO PUBLIC.FRIENDS (USER_ID,FRIEND_ID,FRIENDSHIP)
VALUES (2,1,true);
INSERT INTO PUBLIC.FRIENDS (USER_ID,FRIEND_ID,FRIENDSHIP)
VALUES (2,3,true);
INSERT INTO PUBLIC.FRIENDS (USER_ID,FRIEND_ID)
VALUES (1,3);


INSERT INTO PUBLIC.FILM (NAME,DESCRIPTION,RELEASE_DATE,DURATION, MPAA_id)
VALUES ('Фильм1','какое-то описание','2022-03-15',180,1);
INSERT INTO PUBLIC.FILM (NAME,DESCRIPTION,RELEASE_DATE,DURATION,MPAA_id)
VALUES ('Фильм2','какое-то описание','2022-01-16',120,4);
INSERT INTO PUBLIC.FILM (NAME,DESCRIPTION,RELEASE_DATE,DURATION,MPAA_id)
VALUES ('Фильм3','какое-то описание','2020-08-16',120,5);

INSERT INTO PUBLIC.GENRE_FILM (GENRE_ID,FILM_ID)
VALUES (1,1);
INSERT INTO PUBLIC.GENRE_FILM (GENRE_ID,FILM_ID)
VALUES (3,1);
INSERT INTO PUBLIC.GENRE_FILM (GENRE_ID,FILM_ID)
VALUES (5,2);
INSERT INTO PUBLIC.GENRE_FILM (GENRE_ID,FILM_ID)
VALUES (4,3);
INSERT INTO PUBLIC.GENRE_FILM (GENRE_ID,FILM_ID)
VALUES (2,3);

INSERT INTO PUBLIC.LIKES (USER_ID,FILM_ID)
VALUES (2,3);
INSERT INTO PUBLIC.LIKES (USER_ID,FILM_ID)
VALUES (1,2);
INSERT INTO PUBLIC.LIKES (USER_ID,FILM_ID)
VALUES (2,2);
INSERT INTO PUBLIC.LIKES (USER_ID,FILM_ID)
VALUES (3,1);

INSERT INTO PUBLIC.EVENTS (TIMESTAMP, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID)
 VALUES (123344556, 1, 'LIKE', 'ADD', 1);
INSERT INTO PUBLIC.EVENTS (TIMESTAMP, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID)
VALUES (123344558, 1, 'REVIEW', 'ADD', 1);

INSERT INTO DIRECTORS(NAME)
VALUES ( 'Режиссер1' );
INSERT INTO DIRECTORS(NAME)
VALUES ( 'фильм1' );