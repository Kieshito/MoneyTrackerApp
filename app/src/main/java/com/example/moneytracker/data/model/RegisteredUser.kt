package com.example.moneytracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class RegisteredUser(
    @PrimaryKey(autoGenerate = true) var userId: Int?,
    val login: String,
    val preferredTreatment: String,
    val password: String //храним и сравниваем хеш введенного пароля
)
