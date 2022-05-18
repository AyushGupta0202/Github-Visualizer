package com.eggdevs.githubvisualizer.repository.model

import android.os.Parcelable
import com.eggdevs.githubvisualizer.repository.model.itemresponse.Owner
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepoListItem(
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("owner") val owner: Owner?
): Parcelable
