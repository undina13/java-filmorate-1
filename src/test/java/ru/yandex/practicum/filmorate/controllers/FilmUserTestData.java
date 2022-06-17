package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAA;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;

public class FilmUserTestData {

    public static Film film1 = new Film(1, "New film", "Some description", new Date(2020, 10, 13 ),120, new MPAA(1, "G"));
    public static Film film2 = new Film(2, "New film2", "Some description", new Date(2020, 10, 13),120, new MPAA(1, "G"));
    public static Film film3 = new Film(3, "New film3", "Some description", new Date(2020, 10, 13), 120, new MPAA(1, "G"));
    public static Film film4 = new Film(4, "New film4", "Some description", new Date(2020, 10, 13), 120, new MPAA(1, "G"));
    public static Film film5 = new Film(5, "New film5", "Some description", new Date(2020, 10, 13), 120, new MPAA(1, "G"));
    public static Film film6 = new Film(6, "New film6", "Some description", new Date(2020, 10, 13), 120, new MPAA(1, "G"));
    public static Film film7 = new Film(7, "New film7", "Some description", new Date(2020, 10, 13), 120, new MPAA(1, "G"));
    public static Film film8 = new Film(8, "New film8", "Some description", new Date(2020, 10, 13), 120, new MPAA(1, "G"));
    public static Film film9 = new Film(9, "New film9", "Some description", new Date(2020, 10, 13), 120, new MPAA(1, "G"));
    public static Film film10 = new Film(10, "New film10", "Some description", new Date(2020, 10, 13), 120, new MPAA(1, "G"));
    public static Film film11 = new Film(11, "New film11", "Some description", new Date(2020, 10, 13), 120, new MPAA(1, "G"));


    public static User user1 = new User(1, "dfg@mail.ru", "login", "name", new Date(1980, 5, 13));
    public static User user2 = new User(2, "dfg2@mail.ru", "login2", "name2", new Date(1980, 5, 13));
    public static User user3 = new User(3, "dfg3@mail.ru", "login3", "name3", new Date(1980, 5, 13));
    public static User user4 = new User(4, "dfg4@mail.ru", "login4", "name4", new Date(1980, 5, 13));
    public static User user5 = new User(5, "dfg5@mail.ru", "login5", "name5", new Date(1980, 5, 13));
    public static User user6 = new User(6, "dfg6@mail.ru", "login6", "name6", new Date(1980, 5, 13));
    public static User user7 = new User(7, "dfg7@mail.ru", "login7", "name7", new Date(1980, 5, 13));
    public static User user8 = new User(8, "dfg8@mail.ru", "login8", "name8", new Date(1980, 5, 13));
    public static User user9 = new User(9, "dfg9@mail.ru", "login9", "name9", new Date(1980, 5, 13));
    public static User user10 = new User(10, "dfg10@mail.ru", "login10", "name10", new Date(1980, 5, 13));
}
