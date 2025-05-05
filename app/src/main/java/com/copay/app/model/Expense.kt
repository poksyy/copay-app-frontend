package com.copay.app.model

/**
 * Represents an Expense entity used for expense tracking and data transfer.
 * This model contains all necessary information about a group expense,
 * including who paid, the amount, and the date of the expense.
 */

data class Expense(

    val id: Long,
    val name: String,
    val amount: Double,
    val paidBy: String,
    val date: String
)
