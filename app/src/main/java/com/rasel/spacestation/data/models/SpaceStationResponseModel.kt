package com.rasel.spacestation.data.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpaceStationResponseModel(
    @SerializedName("iss_position")
    val issPosition: IssPosition?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("timestamp")
    val timestamp: Int?
) : Parcelable