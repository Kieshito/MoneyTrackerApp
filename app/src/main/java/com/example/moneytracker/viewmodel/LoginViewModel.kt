package com.example.moneytracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneytracker.MainActivity
import com.example.moneytracker.data.TransactionDataBase
import com.example.moneytracker.data.dao.UserDao
import com.example.moneytracker.data.model.RegisteredUser
import kotlinx.coroutines.delay
import java.lang.IllegalArgumentException

class LoginViewModel(private val dao: UserDao): ViewModel() {
    private lateinit var users: List<RegisteredUser>
    suspend fun foundUser(login: String, password: String): Int? {
        Thread{
            users = dao.getAllUsers()
        }.start()
        delay(250)
        users.forEach {
            if (it.login == login && it.password == password){
                MainActivity.enteredName = it.preferredTreatment.toString()
                MainActivity.enteredLogin = it.login
                return it.userId
            }
        }
        return null
    }

    fun login(userId: Int){
        MainActivity.enteredUserId = userId
    }
}


class LoginViewModelFactoty(private val context: Context): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            val dao = TransactionDataBase.getDatabase(context).userDao()
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}