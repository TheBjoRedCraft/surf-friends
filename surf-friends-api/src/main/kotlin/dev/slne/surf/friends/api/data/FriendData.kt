package dev.slne.surf.friends.api.data

import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.util.UUID

data class FriendData (
    val uuid: UUID,

    val friends: ObjectSet<FriendUser> = mutableObjectSetOf(),
    val friendRequests: ObjectSet<FriendUser> = mutableObjectSetOf(),

    val announcements: Boolean = true,
    val announcementSounds: Boolean = true
)
