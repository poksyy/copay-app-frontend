package com.copay.app.utils

import com.copay.app.dto.expense.response.GetExpenseResponseDTO
import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO

class ExpenseUtils {
    companion object {
        fun calculateMemberExpense(member: Any, expenses: List<GetExpenseResponseDTO>): Double {
            return expenses.sumOf { expense ->
                when (member) {
                    is RegisteredMemberDTO -> {
                        val paid = if (expense.creditorUserId == member.registeredMemberId) {
                            -expense.totalAmount
                        } else 0.0

                        val owes = expense.registeredMembers
                            .filter { it.debtorUserId == member.registeredMemberId }
                            .sumOf { it.amount }

                        paid + owes
                    }

                    is ExternalMemberDTO -> {
                        val paid = if (expense.creditorExternalMemberId == member.externalMembersId) {
                            -expense.totalAmount
                        } else 0.0

                        val owes = expense.externalMembers
                            .filter { it.debtorExternalMemberId == member.externalMembersId }
                            .sumOf { it.amount }

                        paid + owes
                    }

                    else -> 0.0
                }
            }
        }
    }
}