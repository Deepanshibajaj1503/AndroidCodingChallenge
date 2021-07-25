package com.deepanshi.androidcodingchallenge.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Albums")
data class Album(

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("id")
    @PrimaryKey
    val id: Int,

    @SerializedName("title")
    val title: String
)