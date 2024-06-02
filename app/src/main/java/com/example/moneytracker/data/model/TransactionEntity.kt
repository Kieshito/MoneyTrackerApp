package com.example.moneytracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val amount: Double,
    val date: String,
    val description: String,
    val type: String
)