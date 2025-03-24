package dev.slne.surf.friends.api.data

import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.util.UUID

data class FriendData (
    /**
     * The unique identifier for the user.
     */
    val uuid: UUID,

    /**
     * A set of friends associated with the user.
     */
    val friends: ObjectSet<FriendUser> = mutableObjectSetOf(),

    /**
     * A set of friend requests received by the user, sent by others.
     */
    val friendRequests: ObjectSet<FriendUser> = mutableObjectSetOf(),

    /**
     * A set of open friend requests sent by the user.
     */
    val openFriendRequests: ObjectSet<FriendUser> = mutableObjectSetOf(),

    /**
     * Indicates whether the user has enabled announcements.
     */
    var announcements: Boolean = true,

    /**
     * Indicates whether the user has enabled announcement sounds.
     */
    var announcementSounds: Boolean = true
)
