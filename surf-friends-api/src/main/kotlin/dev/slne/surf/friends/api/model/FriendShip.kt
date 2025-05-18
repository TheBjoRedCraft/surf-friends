package dev.slne.surf.friends.api.model

import java.util.UUID

interface FriendShip {
    val userUuid: UUID
    val friendUuid: UUID
    val createdAt: Long
}