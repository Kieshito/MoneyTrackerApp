package com.example.moneytracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.moneytracker.data.dao.TransactionDao
import com.example.moneytracker.data.model.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TransactionEntity::class], version = 1 )
abstract class TransactionDataBase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

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
                        val dao = getDatabase(context).transactionDao()
                        dao.insertTransaction(TransactionEntity(1, "Salary", 5000.0, "Today", "asd", "Income"))
                        dao.insertTransaction(TransactionEntity(2, "Shopping", 3000.0, "Tomorrow", "asd", "Expense"))
                    }
                }
            }).build()
        }
    }
}