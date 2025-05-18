package dev.slne.surf.friends.api.model

import java.util.UUID

interface FriendRequest {
    val senderUuid: UUID
    val receiverUuid: UUID
    val sentAt: Long
}