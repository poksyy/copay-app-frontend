package com.copay.app.utils.session

import com.copay.app.model.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupSession @Inject constructor() {
    private val _group = MutableStateFlow<Group?>(null)
    val group: StateFlow<Group?> get() = _group

    fun setGroup(group: Group) {
        _group.value = group
    }

    fun clearGroup() {
        _group.value = null
    }
}