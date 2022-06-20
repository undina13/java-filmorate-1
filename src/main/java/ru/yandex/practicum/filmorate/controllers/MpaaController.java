package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPAA;
import ru.yandex.practicum.filmorate.service.MpaaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@Getter
public class MpaaController {
    MpaaService mpaaService;

    @Autowired
    public MpaaController(MpaaService mpaaService) {
        this.mpaaService = mpaaService;
    }

    @GetMapping
    public Collection<MPAA> findAll() {
        return mpaaService.getAll();
    }

    @GetMapping("/{id}")
    public MPAA getMpaaById(@PathVariable int id) {
        return mpaaService.getById(id);
    }
}
