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


class EditProfileViewModel(private val daoUser: UserDao): ViewModel(){
    private lateinit var users: List<RegisteredUser>

    suspend fun updateUser(user: RegisteredUser): Boolean{
        if (user.preferredTreatment!="" && user.password!="" && user.login!=""){
            daoUser.updateUser(user)
            return true
        }
        return false
    }

    suspend fun foundUser(): RegisteredUser {
        Thread{
            users = daoUser.getAllUsers()
        }.start()
        delay(100)
        var result = RegisteredUser(null,"","","")
        users.forEach {
            if (it.userId == MainActivity.enteredUserId) result = it
        }
        return result
    }
}


class EditProfileViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)){
            val daoU = TransactionDataBase.getDatabase(context).userDao()
            @Suppress("UNCHECKED_CAST")
            return EditProfileViewModel(daoU) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}