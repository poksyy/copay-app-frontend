package com.copay.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.copay.app.repository.GroupRepository
import com.copay.app.utils.state.AuthState
import com.copay.app.utils.state.GroupState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    init {
        Log.d("GroupViewModel", "GroupViewModel created")
    }

    private val _groupState = MutableStateFlow<GroupState>(GroupState.Idle)
    val groupState: MutableStateFlow<GroupState> get() = _groupState

    // Method to create a group.
    fun createGroup(
        context: Context,
        groupName: String,
        groupDescription: String,
        estimatedPrice: Float,
        currency: String,
        invitedExternalMembers: List<String>,
        invitedCopayMembers: List<String>
    ) {
        viewModelScope.launch {

            _groupState.value = GroupState.Loading
            // Stores the response from backend.
            val backendResponse = groupRepository.createGroup(
                context,
                groupName,
                groupDescription,
                estimatedPrice,
                currency,
                invitedExternalMembers,
                invitedCopayMembers
            )
            // Updates the UI trough authState.
//            _groupState.value = backendResponse

            val result = groupRepository.createGroup(
                context,
                groupName,
                groupDescription,
                estimatedPrice,
                currency,
                invitedExternalMembers,
                invitedCopayMembers
            )


        }
    }
}
