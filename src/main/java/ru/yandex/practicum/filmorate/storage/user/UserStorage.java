package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
     User create(User user);

     User put(User user);

     Collection<User> getAll();

    Optional<User> get(int id);

     //for testing
     void deleteAll();

    List<User> getFriends(int id);

    void addFriends(int id, int friendId);

    void deleteFriends(int id, int friendId);

    List<User> getCommonFriends(int id, int otherId);
}

