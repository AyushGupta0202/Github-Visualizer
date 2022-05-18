package com.eggdevs.githubvisualizer.repository

import com.eggdevs.githubvisualizer.repository.server.RetrofitInstance

class CommonRepository {

    suspend fun getRepoList(searchQuery: String) = RetrofitInstance.networkService.getRepoList(searchQuery)

    suspend fun getIssuesInRepo(repoName: String) = RetrofitInstance.networkService.getIssuesInRepo(repoName)
}