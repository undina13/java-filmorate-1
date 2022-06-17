package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Qualifier("inMemoryUserStorage")
    UserStorage userStorage;

    @Autowired

    public UserService(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {

        this.userStorage = userStorage;
    }

//    public void addFriends(int user1Id, int user2Id) {
//        userStorage.get(user1Id).getFriends().add(userStorage.get(user2Id).getId());
//        userStorage.get(user2Id).getFriends().add(userStorage.get(user1Id).getId());
//    }

//    public List<User> getFriends(int id) {
//        return userStorage.get(id).getFriends()
//                .stream()
//                .map(idFriend -> userStorage.get(idFriend))
//                .collect(Collectors.toList());
//    }

//    public void deleteFriends(int user1Id, int user2Id) {
//        userStorage.get(user1Id).getFriends().remove(userStorage.get(user2Id).getId());
//        userStorage.get(user2Id).getFriends().remove(userStorage.get(user1Id).getId());
//    }
//
//    public List<User> getCommonFriends(int user1Id, int user2Id) {
//        Set<Integer> setMutualFriends = userStorage.get(user1Id).getFriends();
//        setMutualFriends.retainAll(userStorage.get(user2Id).getFriends());
//        return setMutualFriends
//                .stream()
//                .map(idFriend -> userStorage.get(idFriend).get())
//                .collect(Collectors.toList());
//    }
}
