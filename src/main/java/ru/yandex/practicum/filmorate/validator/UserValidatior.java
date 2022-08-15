package ru.yandex.practicum.filmorate.validator;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

@UtilityClass
public class UserValidatior {
    public void validate(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getId() < 0) {
            throw new UserNotFoundException("Неверный айди юзера");
        }
    }
}
