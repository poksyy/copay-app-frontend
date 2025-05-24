package com.copay.app.ui.components

sealed class GroupMember {
    abstract fun displayText(): String
    abstract val identifier: String

    data class Me(val phoneNumber: String?) : GroupMember() {
        override fun displayText(): String = "Me"
        override val identifier: String = phoneNumber ?: "me"
    }

    data class RegisteredMember(val name: String, val phoneNumber: String) : GroupMember() {
        override fun displayText(): String = name
        override val identifier: String = phoneNumber
    }

    data class ExternalMember(val name: String) : GroupMember() {
        override fun displayText(): String = "$name (External)"
        override val identifier: String = name
    }
}
