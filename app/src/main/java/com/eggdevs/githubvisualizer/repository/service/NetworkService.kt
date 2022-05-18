package com.eggdevs.githubvisualizer.repository.service

import com.eggdevs.githubvisualizer.repository.model.IssueListItem
import com.eggdevs.githubvisualizer.repository.model.RepoListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    @GET("search/repositories")
    suspend fun getRepoList(@Query("q") searchQuery: String): Response<RepoListResponse>

    @GET("repos/{repo_name}/issues")
    suspend fun getIssuesInRepo(@Path(value = "repo_name", encoded = true) repoName: String): Response<List<IssueListItem>>
}