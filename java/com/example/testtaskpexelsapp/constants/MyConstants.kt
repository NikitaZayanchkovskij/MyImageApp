package com.example.testtaskpexelsapp.constants


object MyConstants {

    const val SPLASH_SCREEN_TIMER = 3000L
    const val MY_ROOM_DATABASE_NAME = "PEXELS_APP_ROOM_DATABASE.db"
    const val MY_ROOM_TABLE_NAME = "Bookmarks"

    /* Это мой ключ с сайта https://www.pexels.com/ru-ru/api/documentation/, зарегистрировался там
     * т.к. ключ выдаётся только зарегистрированным пользователям.
     * Ключ нужен для обращения к серверу и получения с него данных.
     */
    const val MY_PEXELS_API_KEY = "E9zhbFt21lBC62q8hXO1SPxZPVV93zQW8Ydpje0QE6Ana1uMt2s1a7GJ"
    const val PEXELS_API_BASE_URL = "https://api.pexels.com/v1/"

}