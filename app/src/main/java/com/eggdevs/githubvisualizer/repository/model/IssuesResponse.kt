package com.eggdevs.githubvisualizer.repository.model

import com.google.gson.annotations.SerializedName

data class IssueListItem(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("state") val state: String?
) {
    val issueState: Boolean = state?.let { it == "open" } ?: false
}
