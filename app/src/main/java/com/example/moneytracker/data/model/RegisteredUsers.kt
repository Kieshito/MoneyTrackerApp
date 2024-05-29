package com.example.moneytracker.data.model

data class RegisteredUsers(
    val login: String, //fk
    val firstName: String,
    val lastName: String,
    val password: String //храним и сравниваем хеш введенного пароля
)
