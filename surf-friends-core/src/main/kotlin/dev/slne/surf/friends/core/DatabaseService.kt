package dev.slne.surf.friends.core

import dev.slne.surf.friends.api.data.FriendData
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.UUID

interface DatabaseService {
    fun connect()

    suspend fun loadData(uuid: UUID): FriendData
    suspend fun saveData(data: FriendData)

    suspend fun getData(uuid: UUID): FriendData?

    fun disconnect()

    companion object {
        val INSTANCE: DatabaseService = requiredService<DatabaseService>()
    }
}

val databaseService get() = DatabaseService.INSTANCE