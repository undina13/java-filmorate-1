package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    User put(User user);

    Collection<User> getAll();

    Optional<User> get(int id);

    List<User> getFriends(int id);

    List<User> getCommonFriends(int id, int otherId);
}

