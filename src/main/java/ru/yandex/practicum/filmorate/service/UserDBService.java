package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserDBService {

    UserStorage userStorage;

    @Autowired
    public UserDBService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public Optional<User> get(int id) {
        if (id <= 0) {
            throw new UserNotFoundException("Неверный айди юзера");
        } else
            return userStorage.get(id);
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public void addFriends(int id, int friendId) {
        if (id < 1 || friendId < 1) {
            throw new UserNotFoundException("user not found");
        }
        userStorage.addFriends(id, friendId);
    }

    public void deleteFriends(int id, int friendId) {
        if (id < 1 || friendId < 1) {
            throw new UserNotFoundException("user not found");
        }
        userStorage.deleteFriends(id, friendId);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        if (id < 1 || otherId < 1) {
            throw new UserNotFoundException("user not found");
        }
        return userStorage.getCommonFriends(id, otherId);
    }

    public User put(User user) {
        return userStorage.put(user);
    }

    public User create(User user) {
        return userStorage.create(user);
    }
}
