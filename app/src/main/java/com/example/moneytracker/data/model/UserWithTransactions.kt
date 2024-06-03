package com.example.moneytracker.data.model

import androidx.room.Embedded
import androidx.room.Relation


//One to many Relationship DB
data class UserWithTransactions(
    @Embedded val user: RegisteredUser,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userOwnerId"
    )
    val transactions: List<TransactionEntity>
)