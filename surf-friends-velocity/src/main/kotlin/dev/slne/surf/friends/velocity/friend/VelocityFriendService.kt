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

        if (targetData.friendRequests.contains(sender)) {
            sender.sendText(buildText {
                error("Du hast bereits eine Freundschaftsanfrage an diesen Spieler gesendet.")
            })
            return
        }

        if(senderData.friendRequests.contains(target)) {
            sender.sendText(buildText {
                error("Du hast bereits eine Freundschaftsanfrage von diesem Spieler erhalten.")
            })
            return
        }

        if(senderData.friends.contains(target)) {
            sender.sendText(buildText {
                error("Du bist bereits mit diesem Spieler befreundet.")
            })
            return
        }

        if(targetData.friends.contains(sender)) {
            sender.sendText(buildText {
                error("Dieser Spieler ist bereits dein Freund.")
            })
            return
        }

        targetData.edit {
            friendRequests.add(sender)
        }

        senderData.edit {
            openFriendRequests.add(target)
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

        if(!targetData.friendRequests.contains(sender)) {
            sender.sendText(buildText {
                error("Du hast keine Freundschaftsanfrage von diesem Spieler erhalten.")
            })
            return
        }

        if(senderData.friends.contains(target)) {
            sender.sendText(buildText {
                error("Du bist bereits mit diesem Spieler befreundet.")
            })
            return
        }

        targetData.edit {
            friendRequests.remove(sender)
            friends.add(sender)
        }

        senderData.edit {
            openFriendRequests.remove(target)
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

        if(!targetData.friendRequests.contains(sender)) {
            sender.sendText(buildText {
                error("Du hast keine Freundschaftsanfrage von diesem Spieler erhalten.")
            })
            return
        }

        targetData.edit {
            friendRequests.remove(sender)
        }

        senderData.edit {
            openFriendRequests.remove(target)
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

        if(!senderData.openFriendRequests.contains(target)) {
            sender.sendText(buildText {
                error("Du hast keine Freundschaftsanfrage an diesen Spieler gesendet.")
            })
            return
        }

        senderData.edit {
            openFriendRequests.remove(target)
        }

        targetData.edit {
            friendRequests.remove(sender)
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

        if(playerData.friends.contains(target)) {
            player.sendText(buildText {
                error("Du bist bereits mit diesem Spieler befreundet.")
            })
            return
        }

        if(targetData.friends.contains(player)) {
            player.sendText(buildText {
                error("Dieser Spieler ist bereits dein Freund.")
            })
            return
        }

        playerData.edit {
            friends.add(target)
        }

        targetData.edit {
            friends.add(player)
        }
    }

    override suspend fun breakFriendShip(player: FriendUser, friend: FriendUser) {
        val targetData = friend.friendData
        val playerData = player.friendData

        if(!playerData.friends.contains(friend)) {
            player.sendText(buildText {
                error("Du bist nicht mit diesem Spieler befreundet.")
            })
            return
        }

        playerData.edit {
            friends.remove(friend)
        }

        targetData.edit {
            friends.remove(player)
        }

        player.sendText(buildText {
            primary("Du hast ")
            secondary("die Freundschaft")
            primary(" mit ${targetData.username}")
            secondary(" beendet.")
        })
    }

    override suspend fun getFriends(player: FriendUser): ObjectSet<FriendUser> = player.friendData.friends

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