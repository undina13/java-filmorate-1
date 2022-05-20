# java-filmorate
Template repository for Filmorate project.


![img.png](src/main/resources/img.png)

получить N первых фильмов по количеству лайков

SELECT film_id, COUNT(user_id)
FROM likes
GROUP BY film_id
ORDER BY COUNT(user_id) DESC
LIMIT N;

получить друзей юзера user_id
SELECT * FROM user
WHERE id IN(SELECT user1_id FROM  friends WHERE user2_id = user_id);


получить общих друзей iduser1 и iduser2
SELECT * 
FROM user
WHERE id IN(SELECT user1_id FROM  friends WHERE user2_id = iduser1)
AND id IN(SELECT user1_id FROM  friends WHERE user2_id = iduser2);


