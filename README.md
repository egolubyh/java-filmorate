# java-filmorate
Template repository for Filmorate project.

![Схема базы данных](/QuickDBD-filmorate.png)

### Примеры запросов:
```roomsql
-- Запрос на получение всех имеющихся пользователей в БД
SELECT *
FROM filmorate.user
```
```roomsql
-- Запрос на получение всех имеющихся фильмов в БД
SELECT
    f.id,
    f.title,
    f.description,
    f.release_date,
    f.duration,
    r.title AS rating
FROM filmorate.film AS f
JOIN filmorate.rating AS r ON f.rating = r.id;
```

```roomsql
-- Запрос на получение 5 наиболее популярных фильмов имеющихся в БД
SELECT 
	f.id,
	f.title,
	f.description,
	f.release_date,
	f.duration,
	r.title,
	COUNT(l.user_id) AS likes
FROM filmorate.film AS f
JOIN filmorate.rating AS r ON f.rating = r.id
JOIN filmorate.like AS l ON f.id = l.film_id
GROUP BY f.id, r.title
ORDER BY likes DESC
LIMIT 5;
```
```roomsql
-- Запрос на получение общих друзей пользователя id = 1 с другом id = 3
SELECT 
	f.friend_two AS friend_id,
	u.name
FROM filmorate.friend AS f
JOIN filmorate.user AS u ON f.friend_two = u.id
WHERE f.confirmed
	AND f.friend_one = 1
	OR f.friend_one = 3
GROUP BY friend_id, u.name
HAVING COUNT(f.friend_two) = 2
```
