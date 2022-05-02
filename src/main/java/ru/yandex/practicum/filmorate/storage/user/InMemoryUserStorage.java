package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public User get(int id) {
        if(!users.containsKey(id)){
            throw new UserNotFoundException("User with id = " + id + " not found");
        }
        return users.get(id);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }
}
