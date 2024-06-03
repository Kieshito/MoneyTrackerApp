package com.example.moneytracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneytracker.MainActivity
import com.example.moneytracker.data.TransactionDataBase
import com.example.moneytracker.data.dao.UserDao
import com.example.moneytracker.data.model.RegisteredUser
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.lang.IllegalArgumentException

class RegistrationViewModel(private val dao: UserDao): ViewModel() {
    private lateinit var users: List<RegisteredUser>
    suspend fun checkUser(user: RegisteredUser): Boolean {
        Thread{
            users = dao.getAllUsers()
        }.start()
        delay(1000)
        users.forEach {
            if (it.password == user.password &&
                it.login == user.login &&
                it.preferredTreatment == user.preferredTreatment) return false

        }
        return true
    }

    suspend fun registration(user: RegisteredUser){
        coroutineScope {
            dao.insertUser(user)
        }
        Thread {
            users = dao.getAllUsers()
        }.start()
        delay(1000)
        users.forEach {
            if (it.password == user.password &&
                it.login == user.login &&
                it.preferredTreatment == user.preferredTreatment){
                MainActivity.enteredUserId = it.userId!!
                MainActivity.enteredName = it.preferredTreatment.toString()
            }
        }
    }
}


class RegistrationViewModelFactoty(private val context: Context): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)){
            val dao = TransactionDataBase.getDatabase(context).userDao()
            @Suppress("UNCHECKED_CAST")
            return RegistrationViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}