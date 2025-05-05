package dev.slne.surf.friends.core

import dev.slne.surf.friends.api.data.FriendData
import dev.slne.surf.friends.core.type.SearchType
import dev.slne.surf.surfapi.core.api.util.requiredService

interface DatabaseService {
    fun connect()

    suspend fun loadData(type: SearchType, value: String): FriendData?
    suspend fun saveData(data: FriendData)
    suspend fun getData(type: SearchType, value: String): FriendData?

    fun updateData(data: FriendData)
    fun disconnect()

    companion object {
        val INSTANCE: DatabaseService = requiredService<DatabaseService>()
    }
}

val databaseService get() = DatabaseService.INSTANCE