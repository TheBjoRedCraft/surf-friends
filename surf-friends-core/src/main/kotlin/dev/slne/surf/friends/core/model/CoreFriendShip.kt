package dev.slne.surf.friends.core.model

import dev.slne.surf.friends.api.model.FriendShip
import java.util.UUID

class CoreFriendShip(
    override val userUuid: UUID,
    override val friendUuid: UUID,
    override val createdAt: Long
) : FriendShip {
}