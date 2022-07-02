package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.MPAA;
import ru.yandex.practicum.filmorate.storage.MPAADbStorage;

import java.util.Collection;

@Service
public class MpaaService {
    private MPAADbStorage mpaaDbStorage;

    @Autowired
    public MpaaService(MPAADbStorage mpaaDbStorage) {
        this.mpaaDbStorage = mpaaDbStorage;
    }

    public Collection<MPAA> getAll() {
        return mpaaDbStorage.getAll();
    }

    public MPAA getById(int id) {
        if (id < 1) {
            throw new UserNotFoundException("not found mpaa id");
        }
        return mpaaDbStorage.getById(id);
    }
}
