# Filmorate
Бекэнд социальной сети, которая поможет выбрать кино на основе того, какие фильмы вы и ваши друзья смотрите и какие оценки им ставите

## Возможности приложения:

* создание, обновление фильмов;
* возможность отметки понравившихся фильмов;
* вывод лучших фильмов (в зависимости от количества лайков);
* создание и обновление пользователей;
* добавление пользователей в друзья;
* удаление пользователей из друзей.

## В командной работе добавлены 
Функциональность «Отзывы».
Функциональность «Поиск».
Функциональность «Общие фильмы».
Функциональность «Рекомендации».
Функциональность «Лента событий».
Функциональность «Популярные фильмы», которая предусматривает вывод самых любимых у зрителей фильмов по жанрам и годам.
Функциональность «Фильмы по режиссёрам», которая предполагает добавление к фильму информации о его режиссёре.

 В ветке add-marks проведен рефакторинг
 Лайки замеенены на оценки от 1 до 10 включительно. Оценка от 1 до 5 считается отрицательной, оценка от 6 до 10 — положительной. 
Изменилась система рейтинга — он вычисляется как среднее арифметическое.
Рекомендации — поиск пользователей с похожими оценками одним и тем же фильмам. Рекомендуются фильмы только с положительной оценкой.


 
 
 
![img.png](src/main/resources/img.png)

получить N первых фильмов по количеству лайков

SELECT film_id, COUNT(user_id)
FROM likes
GROUP BY film_id
ORDER BY COUNT(user_id) DESC
LIMIT N;

получить друзей юзера user1_id
SELECT * FROM user
WHERE id IN(SELECT user_id FROM  friends WHERE friend_id = user1_id);


получить общих друзей iduser1 и iduser2
SELECT * 
FROM user
WHERE id IN(SELECT user_id FROM  friends WHERE friend_id = iduser1)
AND id IN(SELECT user_id FROM  friends WHERE friend_id = iduser2);


