package com.copay.app.model

import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO

/**
 * Represents a Group entity used for group management and data transfer.
 * This model contains all group information including members and metadata.
 */
data class Group(

    val groupId: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val estimatedPrice: Float? = null,
    val currency: String? = null,
    val createdAt: String? = null,
    val isOwner: Boolean? = null,
    val ownerId: Long? = null,
    val ownerName: String? = null,
    val registeredMembers: List<RegisteredMemberDTO>? = null,
    val externalMembers: List<ExternalMemberDTO>? = null,
    val imageUrl: String? = null,
    val imageProvider: String? = null,
    val expenses: List<Expense>? = null
) {
    data class Member(
        val id: Long,
        val name: String,
        val isRegistered: Boolean,
        val phoneNumber: String? = null
    )
}