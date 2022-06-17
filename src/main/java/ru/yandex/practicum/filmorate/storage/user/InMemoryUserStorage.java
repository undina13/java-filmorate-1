package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User create(User user) {
       users.put(user.getId(), user);
       return user;
           }

    @Override
    public User put(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getAll() {
       return users.values();
    }

    @Override
    public Optional<User> get(int id) {
        if(!users.containsKey(id)){
            throw new UserNotFoundException("User with id = " + id + " not found");
        }
        return Optional.of(users.get(id));
    }

    @Override
    public List<User> getFriends(int id) {
        return null;
    }

    @Override
    public void addFriends(int id, int friendId) {

    }

    @Override
    public void deleteFriends(int id, int friendId) {

    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        return null;
    }
}
