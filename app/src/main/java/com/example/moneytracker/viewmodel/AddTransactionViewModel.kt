package com.example.moneytracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneytracker.data.TransactionDataBase
import com.example.moneytracker.data.dao.TransactionDao
import com.example.moneytracker.data.model.TransactionEntity
import java.lang.IllegalArgumentException

class AddTransactionViewModel(val dao: TransactionDao) : ViewModel() {

    suspend fun addTransaction(transactionEntity: TransactionEntity): Boolean {
        if (!validateEntity(transactionEntity)) return false
        return try {
            dao.insertTransaction(transactionEntity)
            true
        } catch (ex: Throwable) {
            false
        }
    }

    fun validateEntity(transactionEntity: TransactionEntity): Boolean {
        return !(transactionEntity.type != "Income" && transactionEntity.type != "Expense" || transactionEntity.type.isEmpty() ||
                transactionEntity.name == "None" || transactionEntity.name.isEmpty() ||
                transactionEntity.amount.isNaN() || transactionEntity.amount.compareTo(0L) < 0)
    }

    fun valudateSMTH(it: Any?) {

    }
}

class AddTransactionViewModelFactoty(private val context: Context): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTransactionViewModel::class.java)){
            val dao = TransactionDataBase.getDatabase(context).transactionDao()
            @Suppress("UNCHECKED_CAST")
            return AddTransactionViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
