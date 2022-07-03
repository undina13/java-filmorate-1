
MERGE INTO PUBLIC.GENRE key (GENRE_ID) values (1, 'Комедия');
MERGE INTO PUBLIC.GENRE key (GENRE_ID) values (2, 'Драма');
MERGE INTO PUBLIC.GENRE key (GENRE_ID) values (3, 'Мультфильм');
MERGE INTO PUBLIC.GENRE key (GENRE_ID) values (4, 'Триллер');
MERGE INTO PUBLIC.GENRE key (GENRE_ID) values (5, 'Документальный');
MERGE INTO PUBLIC.GENRE key (GENRE_ID) values (6, 'Боевик');

MERGE INTO PUBLIC.MPAA key (MPAA_ID) values (1, 'G');     --  у фильма нет возрастных ограничений
MERGE INTO PUBLIC.MPAA key (MPAA_ID) values (2, 'PG');    -- детям рекомендуется смотреть фильм с родителями
MERGE INTO PUBLIC.MPAA key (MPAA_ID) values (3, 'PG-13'); -- детям до 13 лет просмотр не желателен
MERGE INTO PUBLIC.MPAA key (MPAA_ID) values (4, 'R');     --  лицам до 17 лет просматривать фильм можно только в присутствии взрослого
MERGE INTO PUBLIC.MPAA key (MPAA_ID) values (5, 'NC-17'); -- лицам до 18 лет просмотр запрещён

