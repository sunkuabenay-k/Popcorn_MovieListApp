package com.example.movielist.data.local
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "favorites",
    primaryKeys = ["userId", "movieId"],
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userId"]),
        ForeignKey(entity = MovieEntity::class, parentColumns = ["id"], childColumns = ["movieId"])
    ]
)
data class FavoriteEntity(
    val userId: Int,
    val movieId: Int
)