package com.example.moneytracker.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table",
    foreignKeys = [ForeignKey(entity = RegisteredUser::class, parentColumns = ["userId"], childColumns = ["userOwnerId"], onDelete = ForeignKey.CASCADE)]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val transactionId: Int?,
    val userOwnerId: Int,
    val name: String,
    val amount: Double,
    val date: String,
    val description: String,
    val type: String
)