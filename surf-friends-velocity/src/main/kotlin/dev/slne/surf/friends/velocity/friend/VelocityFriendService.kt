package dev.slne.surf.friends.velocity.friend

import com.google.auto.service.AutoService
import dev.slne.surf.friends.api.user.FriendUser
import dev.slne.surf.friends.core.FriendService
import dev.slne.surf.friends.core.databaseService
import dev.slne.surf.friends.velocity.util.edit
import dev.slne.surf.friends.velocity.util.sendText
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.kyori.adventure.util.Services.Fallback
import java.util.UUID

@AutoService(FriendService::class)
class VelocityFriendService(): FriendService, Fallback {
    override suspend fun sendFriendRequest(sender: FriendUser, target: FriendUser) {
        val targetData = target.friendData
        val senderData = sender.friendData

        if (targetData.friendRequests.contains(senderData.uuid)) {
            sender.sendText(buildText {
                error("Du hast bereits eine Freundschaftsanfrage an diesen Spieler gesendet.")
            })
            return
        }

        if(senderData.friendRequests.contains(targetData.uuid)) {
            sender.sendText(buildText {
                error("Du hast bereits eine Freundschaftsanfrage von diesem Spieler erhalten.")
            })
            return
        }

        if(senderData.friends.contains(targetData.uuid)) {
            sender.sendText(buildText {
                error("Du bist bereits mit diesem Spieler befreundet.")
            })
            return
        }

        if(targetData.friends.contains(senderData.uuid)) {
            sender.sendText(buildText {
                error("Dieser Spieler ist bereits dein Freund.")
            })
            return
        }

        targetData.edit {
            friendRequests.add(senderData.uuid)
        }

        senderData.edit {
            openFriendRequests.add(targetData.uuid)
        }
        sender.sendText(buildText {
            primary("Du hast ")
            secondary("eine Freundschaftsanfrage")
            primary(" an ${targetData.username}")
            secondary(" gesendet.")
        })
    }

    override suspend fun acceptFriendRequest(sender: FriendUser, target: FriendUser) {
        val targetData = target.friendData
        val senderData = sender.friendData

        if(!targetData.friendRequests.contains(senderData.uuid)) {
            sender.sendText(buildText {
                error("Du hast keine Freundschaftsanfrage von diesem Spieler erhalten.")
            })
            return
        }

        if(senderData.friends.contains(targetData.uuid)) {
            sender.sendText(buildText {
                error("Du bist bereits mit diesem Spieler befreundet.")
            })
            return
        }

        targetData.edit {
            friendRequests.remove(senderData.uuid)
            friends.add(senderData.uuid)
        }

        senderData.edit {
            openFriendRequests.remove(targetData.uuid)
        }

        sender.sendText(buildText {
            primary("Du hast ")
            secondary("die Freundschaftsanfrage")
            primary(" von ${targetData.username}")
            secondary(" angenommen.")
        })
    }

    override suspend fun declineFriendRequest(sender: FriendUser, target: FriendUser) {
        val targetData = target.friendData
        val senderData = sender.friendData

        if(!targetData.friendRequests.contains(senderData.uuid)) {
            sender.sendText(buildText {
                error("Du hast keine Freundschaftsanfrage von diesem Spieler erhalten.")
            })
            return
        }

        targetData.edit {
            friendRequests.remove(senderData.uuid)
        }

        senderData.edit {
            openFriendRequests.remove(targetData.uuid)
        }

        sender.sendText(buildText {
            primary("Du hast ")
            secondary("die Freundschaftsanfrage")
            primary(" von ${targetData.username}")
            secondary(" abgelehnt.")
        })
    }

    override suspend fun revokeFriendRequest(sender: FriendUser, target: FriendUser) {
        val targetData = target.friendData
        val senderData = sender.friendData

        if(!senderData.openFriendRequests.contains(targetData.uuid)) {
            sender.sendText(buildText {
                error("Du hast keine Freundschaftsanfrage an diesen Spieler gesendet.")
            })
            return
        }

        senderData.edit {
            openFriendRequests.remove(targetData.uuid)
        }

        targetData.edit {
            friendRequests.remove(senderData.uuid)
        }

        sender.sendText(buildText {
            primary("Du hast ")
            secondary("die Freundschaftsanfrage")
            primary(" an ${targetData.username}")
            secondary(" zurückgezogen.")
        })
    }

    override suspend fun createFriendShip(player: FriendUser, target: FriendUser) {
        val targetData = target.friendData
        val playerData = player.friendData

        if(playerData.friends.contains(targetData.uuid)) {
            player.sendText(buildText {
                error("Du bist bereits mit diesem Spieler befreundet.")
            })
            return
        }

        if(targetData.friends.contains(playerData.uuid)) {
            player.sendText(buildText {
                error("Dieser Spieler ist bereits dein Freund.")
            })
            return
        }

        playerData.edit {
            friends.add(targetData.uuid)
        }

        targetData.edit {
            friends.add(playerData.uuid)
        }
    }

    override suspend fun breakFriendShip(player: FriendUser, friend: FriendUser) {
        val targetData = friend.friendData
        val playerData = player.friendData

        if(!playerData.friends.contains(targetData.uuid)) {
            player.sendText(buildText {
                error("Du bist nicht mit diesem Spieler befreundet.")
            })
            return
        }

        playerData.edit {
            friends.remove(targetData.uuid)
        }

        targetData.edit {
            friends.remove(playerData.uuid)
        }

        player.sendText(buildText {
            primary("Du hast ")
            secondary("die Freundschaft")
            primary(" mit ${targetData.username}")
            secondary(" beendet.")
        })
    }

    override suspend fun getFriends(player: FriendUser): ObjectSet<UUID> = player.friendData.friends

    override suspend fun toggleAnnouncements(player: FriendUser): Boolean {
        val playerData = player.friendData

        playerData.edit {
            announcements = !announcements
        }

        return playerData.announcements
    }

    override suspend fun toggleAnnouncementSounds(player: FriendUser): Boolean {
        val playerData = player.friendData

        playerData.edit {
            announcementSounds = !announcementSounds
        }

        return playerData.announcementSounds
    }
}