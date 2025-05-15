package com.copay.app.mappers

import com.copay.app.dto.user.UserResponseDTO
import com.copay.app.model.User

fun UserResponseDTO.toUser(): User {
    return User(
        userId = this.id,
        username = this.username,
        phoneNumber = this.phoneNumber,
        email = this.email
    )
}