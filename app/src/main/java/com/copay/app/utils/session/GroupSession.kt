package com.copay.app.utils.session

import com.copay.app.dto.group.auxiliary.ExternalMemberDTO
import com.copay.app.dto.group.auxiliary.RegisteredMemberDTO
import com.copay.app.model.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupSession @Inject constructor() {
    private val _group = MutableStateFlow<Group?>(null)
    val group: StateFlow<Group?> get() = _group

    fun setGroup(
        groupId: Long?,
        groupName: String?,
        description: String?,
        currency: String?,
        registeredMembers: List<RegisteredMemberDTO>?,
        externalMembers: List<ExternalMemberDTO>?
    ) {
        if (groupId != null && groupName != null) {
            _group.value = Group(
                groupId = groupId,
                name = groupName,
                description = description,
                currency = currency,
                registeredMembers = registeredMembers,
                externalMembers = externalMembers
            )
        }
    }

    fun clearGroup() {
        _group.value = null
    }
}