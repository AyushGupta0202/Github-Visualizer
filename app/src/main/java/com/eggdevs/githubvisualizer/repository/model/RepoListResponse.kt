package com.eggdevs.githubvisualizer.repository.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepoListResponse(
    @SerializedName("total_count") var totalCount: Int,
    @SerializedName("incomplete_results") var incompleteResults: Boolean,
    @SerializedName("items") var repoListItems: List<RepoListItem>
): Parcelable
