package com.copay.app.dto.group.auxiliary

/**
 * Data class representing information about a registered member in a group.
 */
data class RegisteredMemberDTO(
    val registeredMemberId: Long,
    val username: String,
    val phoneNumber: String
)
