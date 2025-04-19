package com.copay.app.repository

import android.content.Context
import com.copay.app.service.GroupService
import retrofit2.Response

class GroupRepository(private val groupService: GroupService) {

    fun createGroup(
        context: Context,
        groupName: String,
        groupDescription: String,
        estimatedPrice: Float,
        currency: String,
        members: List<String>,
        phoneNumbers: List<String>
    ): Any {
        return groupService
    }
}

//package com.copay.app.repository

//import android.content.Context
//import android.util.Log
//import com.copay.app.utils.state.GroupState
//import javax.inject.Inject
//import javax.inject.Singleton

//@Singleton
//class GroupRepository @Inject constructor() {
//
//    init {
//        Log.d("GroupRepository", "GroupRepository created")
//    }
//
//    suspend fun createGroup(
//        context: Context,
//        groupName: String,
//        groupDescription: String,
//        estimatedPrice: Float,
//        currency: String,
//        invitedExternalMembers: List<String>,
//        invitedCopayMembers: List<String>
//
//    ): GroupState {
//        return try {
//            Log.d(
//                "GroupRepository",
//                "Creating group with name: $groupName, description: $groupDescription, " +
//                        "price: $estimatedPrice $currency, normal members: invitedExternalMembers, " +
//                        "Copay users: invitedCopayMembers"
//            )
//
//            // TODO: Implement actual API call here
//            // Simulating API call success for now
//            GroupState.Success
//
//        } catch (e: Exception) {
//            Log.e("GroupRepository", "Error creating group", e)
//            GroupState.Error(e.message ?: "Unknown error occurred")
//        }
//    }
//}
