package com.copay.app.dto.group.auxiliary

data class RegisteredMemberDTO(
    val registeredMemberId: Long,
    val username: String,
    val phoneNumber: String,
    val payer: Boolean
)