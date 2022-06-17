//package ru.yandex.practicum.filmorate.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.MPAA;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.sql.Date;
//import java.time.Duration;
//import java.time.LocalDate;
//
//public class FilmUserTestData {
//
//  static   ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//
//    public static Film film1 = new Film(1, "Фильм1", "какое-то описание", new Date(2022, 3, 15 ),180, new MPAA(1, "G"));
//    public static Film film2 = new Film(2, "Фильм2", "какое-то описание", new Date(2022, 1, 16),120, new MPAA(4, "R"));
//    public static Film film3 = new Film(3, "Фильм3", "какое-то описание", new Date(2020, 8, 16), 120, new MPAA(5, "NC-17"));
//
//
//    public static User user1 = new User(1, "dfg@mail.ru", "login", "name", new Date(1980, 5, 13));
//    public static User user2 = new User(2, "dfg2@mail.ru", "login2", "name2", new Date(1980, 5, 13));
//    public static User user3 = new User(3, "dfg3@mail.ru", "login3", "name3", new Date(1980, 5, 13));
//    public static User user4 = new User(4, "dfg4@mail.ru", "login4", "name4", new Date(1980, 5, 13));
//    public static User user5 = new User(5, "dfg5@mail.ru", "login5", "name5", new Date(1980, 5, 13));
//    public static User user6 = new User(6, "dfg6@mail.ru", "login6", "name6", new Date(1980, 5, 13));
//    public static User user7 = new User(7, "dfg7@mail.ru", "login7", "name7", new Date(1980, 5, 13));
//    public static User user8 = new User(8, "dfg8@mail.ru", "login8", "name8", new Date(1980, 5, 13));
//    public static User user9 = new User(9, "dfg9@mail.ru", "login9", "name9", new Date(1980, 5, 13));
//    public static User user10 = new User(10, "dfg10@mail.ru", "login10", "name10", new Date(1980, 5, 13));
//}
