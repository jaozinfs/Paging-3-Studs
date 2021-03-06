package com.jaozinfs.moovs.movies.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_keys")
data class MoviesKeyId(
    @PrimaryKey
    val repoId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)