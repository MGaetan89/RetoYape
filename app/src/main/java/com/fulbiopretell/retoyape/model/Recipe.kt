package com.fulbiopretell.retoyape.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("image")
    var image: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("longitude")
    var longitude: Double? = null,

    @SerializedName("latitude")
    var latitude: Double? = null
)