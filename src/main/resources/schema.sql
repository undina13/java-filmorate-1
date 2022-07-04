drop table if exists FILM, USERS, GENRE_FILM, MARKS, FRIENDS, MPAA, director_film, directors,REVIEWS, EVENTS ;

CREATE TABLE IF NOT EXISTS PUBLIC.MPAA
(
    MPAA_ID INTEGER               NOT NULL,
    NAME    VARCHAR_IGNORECASE(8) NOT NULL,
    CONSTRAINT MPAA_PK PRIMARY KEY (MPAA_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE
(
    GENRE_ID INTEGER                NOT NULL,
    NAME     VARCHAR_IGNORECASE(15) NOT NULL,
    CONSTRAINT GENRE_PK PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM
(
    FILM_ID      INTEGER                NOT NULL AUTO_INCREMENT,
    NAME         VARCHAR_IGNORECASE(30) NOT NULL,
    DESCRIPTION  VARCHAR,
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    MPAA_id      INTEGER,
    CONSTRAINT FILM_PK PRIMARY KEY (FILM_ID),
    CONSTRAINT FILM_FK FOREIGN KEY (MPAA_id) REFERENCES PUBLIC.MPAA (MPAA_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE_FILM
(
    GENRE_ID INTEGER NOT NULL,
    FILM_ID  INTEGER NOT NULL,
    CONSTRAINT GENRE_FILM_PK PRIMARY KEY (GENRE_ID, FILM_ID),
    CONSTRAINT GENRE_FILM_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRE (GENRE_ID),
    CONSTRAINT GENRE_FILM_FK_1 FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILM (FILM_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS
(
    USER_ID  INTEGER     NOT NULL AUTO_INCREMENT,
    EMAIL    VARCHAR(30) NOT NULL,
    LOGIN    VARCHAR(30) NOT NULL,
    NAME     VARCHAR(30),
    BIRTHDAY DATE,
    CONSTRAINT USER_PK PRIMARY KEY (USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.MARKS
(
    USER_ID INTEGER,
    FILM_ID INTEGER,
    MARC INTEGER,
    CONSTRAINT MARKS_PK PRIMARY KEY (USER_ID, FILM_ID),
    CONSTRAINT MARKS_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS (USER_ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT MARKS_FK_1 FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILM (FILM_ID)  ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDS
(
    USER_ID    INTEGER NOT NULL,
    FRIEND_ID  INTEGER NOT NULL,
    FRIENDSHIP BOOLEAN,
    CONSTRAINT FRIENDS_PK PRIMARY KEY (USER_ID, FRIEND_ID),
    CONSTRAINT FRIENDS_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS (USER_ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FRIENDS_FK_1 FOREIGN KEY (FRIEND_ID) REFERENCES PUBLIC.USERS (USER_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

create table if not exists PUBLIC.directors
(
    director_id INTEGER NOT NULL AUTO_INCREMENT,
    name        varchar(50),
    CONSTRAINT director_pk PRIMARY KEY (director_id)
);

create table if not exists PUBLIC.director_film
(
    director_id int references directors (director_id) ON DELETE CASCADE,
    film_id     int references FILM (FILM_ID)
);


CREATE TABLE IF NOT EXISTS PUBLIC.REVIEWS
(
    ReVIEW_ID   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    CONTENT     VARCHAR(256) NOT NULL,
    IS_POSITIVE BOOLEAN,
    USER_ID     INTEGER REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    FILM_ID     INTEGER REFERENCES FILM (FILM_ID) ON DELETE CASCADE,
    USEFUL      INTEGER      NOT NULL DEFAULT 0
);


CREATE TABLE IF NOT EXISTS PUBLIC.EVENTS
(
    EVENT_ID   INTEGER     NOT NULL AUTO_INCREMENT,
    TIMESTAMP  LONG,
    USER_ID    INTEGER     NOT NULL,
    EVENT_TYPE VARCHAR(30) NOT NULL,
    OPERATION  VARCHAR(30) NOT NULL,
    ENTITY_ID  INTEGER     NOT NULL,
    CONSTRAINT EVENTS_PK PRIMARY KEY (EVENT_ID),
    CONSTRAINT EVENTS_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS (USER_ID) ON DELETE CASCADE ON UPDATE CASCADE

);