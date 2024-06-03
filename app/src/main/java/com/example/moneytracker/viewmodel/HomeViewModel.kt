package com.example.moneytracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneytracker.MainActivity
import com.example.moneytracker.R
import com.example.moneytracker.data.TransactionDataBase
import com.example.moneytracker.data.dao.TransactionDao
import com.example.moneytracker.data.dao.UserDao
import com.example.moneytracker.data.model.RegisteredUser
import com.example.moneytracker.data.model.TransactionEntity
import java.lang.IllegalArgumentException

class HomeViewModel(daoTransaction: TransactionDao, daoUser: UserDao): ViewModel() {
    val transactions = daoTransaction.getAllTransactions(MainActivity.enteredUserId)

    fun getBalance(list: List<TransactionEntity>): String{
        var result = 0.0
        for (expense in list) {
            if (expense.type == "Income") {
                result += expense.amount
            } else {
                result -= expense.amount
            }
        }
        return "$ ${result}"
    }

    fun getTotalExpense(list: List<TransactionEntity>): String{
        var result = 0.0
        for (expense in list) {
            if (expense.type == "Expense") {
                result += expense.amount
            }
        }
        return "$ ${result}"
    }

    fun getTotalIncome(list: List<TransactionEntity>): String {
        var result = 0.0
        for (expense in list) {
            if (expense.type == "Income") {
                result += expense.amount
            }
        }
        return "$ ${result}"
    }

    fun getItemIcon(item: TransactionEntity): Int{
        return if (item.type == "Expense"){
            R.drawable.ic_expense_transaction
        } else R.drawable.ic_income_transaction
    }
}

class HomeViewModelFactoty(private val context: Context): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            val daoT = TransactionDataBase.getDatabase(context).transactionDao()
            val daoU = TransactionDataBase.getDatabase(context).userDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(daoT, daoU) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
