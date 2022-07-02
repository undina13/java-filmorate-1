package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserDBService {

    private UserStorage userStorage;
    private FriendsStorage friendsStorage;
    private EventStorage eventStorage;

    @Autowired
    public UserDBService(UserStorage userStorage, FriendsStorage friendsStorage, EventStorage eventStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
        this.eventStorage = eventStorage;
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
       friendsStorage.addFriends(id, friendId);
    }

    public void deleteFriends(int id, int friendId) {
        if (id < 1 || friendId < 1) {
            throw new UserNotFoundException("user not found");
        }
        friendsStorage.deleteFriends(id, friendId);
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

    public List<Event> getEventByUserId(int id) {
      return  eventStorage.getEventByUserId(id);
    }

    public void deleteUser(int id){
        userStorage.deleteUser(id);
    }
}
