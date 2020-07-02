package com.grishma.grishmamoviesapp.model


import com.google.gson.annotations.SerializedName

data class Content(
    var name: String,
    @SerializedName("poster-image")
    val posterImage: String
)