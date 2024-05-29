package com.example.moneytracker.data.model

data class ExpenseEntity(
    val id: Int,
    val name: String, //категория
    val amount: Double,
    val date: Long,
    val description: String,
    val type: String //income or expense
)