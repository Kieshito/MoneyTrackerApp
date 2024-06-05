package com.example.moneytracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.moneytracker.data.dao.TransactionDao
import com.example.moneytracker.data.dao.UserDao
import com.example.moneytracker.data.model.RegisteredUser
import com.example.moneytracker.data.model.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TransactionEntity::class, RegisteredUser::class], version = 1, exportSchema = false)
abstract class TransactionDataBase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun userDao(): UserDao

    companion object{
        const val DATABASE_NAME = "transaction_database"

        @JvmStatic
        fun getDatabase(context: Context): TransactionDataBase{
            return Room.databaseBuilder(
                context,
                TransactionDataBase::class.java,
                DATABASE_NAME
            ).addCallback(object : RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    initBasicData(context)
                }

                fun initBasicData(context: Context){
                    CoroutineScope(Dispatchers.IO).launch {
                        val daoTransaction = getDatabase(context).transactionDao()
                        val daoUser = getDatabase(context).userDao()
                        daoUser.insertUser(RegisteredUser(1,"admin","admin","Admin"))
                        daoUser.insertUser(RegisteredUser(2, "kie", "kie", "Kie"))
                        daoTransaction.insertTransaction(TransactionEntity(1, 1,"Salary", 5000.0, "Today", "asd", "Income"))
                        daoTransaction.insertTransaction(TransactionEntity(2, 1, "Shopping", 3000.0, "Tomorrow", "asd", "Expense"))
                        daoTransaction.insertTransaction(TransactionEntity(5, 1,"Salary", 5000.0, "Today", "asd", "Income"))
                        daoTransaction.insertTransaction(TransactionEntity(6, 1, "Shopping", 3000.0, "Today", "asd", "Expense"))
                        daoTransaction.insertTransaction(TransactionEntity(7, 1,"Salary", 5000.0, "Today", "asd", "Income"))
                        daoTransaction.insertTransaction(TransactionEntity(8, 1, "Shopping", 3000.0, "Today", "asd", "Expense"))
                        daoTransaction.insertTransaction(TransactionEntity(9, 1,"Salary", 5000.0, "Today", "asd", "Income"))
                        daoTransaction.insertTransaction(TransactionEntity(10, 1, "Shopping", 3000.0, "Today", "asd", "Expense"))
                        daoTransaction.insertTransaction(TransactionEntity(11, 1,"Salary", 5000.0, "Today", "asd", "Income"))
                        daoTransaction.insertTransaction(TransactionEntity(12, 1, "Shopping", 3000.0, "Today", "asd", "Expense"))
                        daoTransaction.insertTransaction(TransactionEntity(13, 1,"Salary", 5000.0, "Today", "asd", "Income"))
                        daoTransaction.insertTransaction(TransactionEntity(4, 1, "Shopping", 3000.0, "Tomorrow", "asd", "Expense"))
                        daoTransaction.insertTransaction(TransactionEntity(3, 2,"Salary", 5000.0, "Today", "asd", "Income"))
                    }
                }
            }).build()
        }
    }
}