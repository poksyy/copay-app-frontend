package com.copay.app.model

data class Expense(

    val id: Long,
    val name: String,
    val amount: Double,
    val paidBy: String,
    val date: String
)
