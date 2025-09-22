package com.wynndie.spwallet.sharedCore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.wynndie.spwallet.sharedCore.data.local.entities.AuthedUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun insertAuthedUser(user: AuthedUserEntity)

    @Query("SELECT * FROM autheduserentity")
    fun getAuthedUsers(): Flow<List<AuthedUserEntity>>

    @Delete
    suspend fun deleteAuthedUser(user: AuthedUserEntity)
}