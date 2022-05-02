package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
     User create(User user);

     User put(User user);

     Collection<User> getAll();

     User get(int id);

     //for testing
     void deleteAll();
}
