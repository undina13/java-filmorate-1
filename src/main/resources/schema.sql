


CREATE TABLE IF NOT EXISTS PUBLIC.MPAA (
                             MPAA_ID INTEGER NOT NULL,
                             NAME VARCHAR_IGNORECASE(8) NOT NULL,
                             CONSTRAINT MPAA_PK PRIMARY KEY (MPAA_ID)
) ;

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE (
                              GENRE_ID INTEGER NOT NULL,
                              NAME VARCHAR_IGNORECASE(15) NOT NULL,
                              CONSTRAINT GENRE_PK PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM (
                             FILM_ID INTEGER NOT NULL AUTO_INCREMENT,
                             NAME VARCHAR_IGNORECASE(30) NOT NULL,
                             DESCRIPTION VARCHAR,
                             RELEASE_DATE DATE,
                             DURATION INTEGER,
                             MPAA_id INTEGER,
                             CONSTRAINT FILM_PK PRIMARY KEY (FILM_ID),
                             CONSTRAINT FILM_FK FOREIGN KEY (MPAA_id) REFERENCES PUBLIC.MPAA(MPAA_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE_FILM (
                                   GENRE_ID INTEGER NOT NULL,
                                   FILM_ID INTEGER NOT NULL,
                                   CONSTRAINT GENRE_FILM_PK PRIMARY KEY (GENRE_ID,FILM_ID),
                                   CONSTRAINT GENRE_FILM_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRE(GENRE_ID),
                                   CONSTRAINT GENRE_FILM_FK_1 FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILM(FILM_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
                               USER_ID INTEGER NOT NULL AUTO_INCREMENT,
                               EMAIL VARCHAR(30) NOT NULL,
                               LOGIN VARCHAR(30) NOT NULL,
                               NAME VARCHAR(30),
                               BIRTHDAY DATE,
                               CONSTRAINT USER_PK PRIMARY KEY (USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.LIKES (
                              USER_ID INTEGER,
                              FILM_ID INTEGER,
                              CONSTRAINT LIKES_PK PRIMARY KEY (USER_ID,FILM_ID),
                              CONSTRAINT LIKES_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID),
                              CONSTRAINT LIKES_FK_1 FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILM(FILM_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDS (
                                USER_ID INTEGER NOT NULL,
                                FRIEND_ID INTEGER NOT NULL,
                                FRIENDSHIP BOOLEAN,
                                CONSTRAINT FRIENDS_PK PRIMARY KEY (USER_ID,FRIEND_ID),
                                CONSTRAINT FRIENDS_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID),
                                CONSTRAINT FRIENDS_FK_1 FOREIGN KEY (FRIEND_ID) REFERENCES PUBLIC.USERS(USER_ID)
);