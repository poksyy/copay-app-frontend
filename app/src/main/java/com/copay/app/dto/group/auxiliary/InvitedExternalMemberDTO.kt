package com.copay.app.dto.group.auxiliary

/**
 * Data class representing information about an invited external member for a group.
 */
data class InvitedExternalMemberDTO (
    val name: String,
    val creditor: Boolean
)
