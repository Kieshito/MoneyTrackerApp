package com.example.moneytracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.moneytracker.data.model.RegisteredUser

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: RegisteredUser)

    @Delete
    suspend fun deleteUser(user: RegisteredUser)

    @Update
    suspend fun updateUser(user: RegisteredUser)

    @Transaction
    @Query("SELECT * FROM USERS_TABLE WHERE userId =:userID")
    fun getUserById(userID: Int): RegisteredUser

    @Transaction
    @Query("SELECT * FROM users_table")
    fun getAllUsers() : List<RegisteredUser>
}