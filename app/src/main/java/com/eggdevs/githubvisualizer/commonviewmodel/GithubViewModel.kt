package com.eggdevs.githubvisualizer.commonviewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eggdevs.githubvisualizer.repository.CommonRepository
import com.eggdevs.githubvisualizer.repository.model.IssueListItem
import com.eggdevs.githubvisualizer.repository.model.RepoListResponse
import com.eggdevs.githubvisualizer.util.Resource
import com.eggdevs.githubvisualizer.util.TAG
import kotlinx.coroutines.launch
import retrofit2.Response

class GithubViewModel: ViewModel() {
    val searchResponse :MutableLiveData<Resource<RepoListResponse>> = MutableLiveData()
    val issueResponse: MutableLiveData<Resource<List<IssueListItem>>> = MutableLiveData()
    private val repository = CommonRepository()

    fun getRepoList(searchQuery: String) {
        viewModelScope.launch {
            try {
                searchResponse.postValue(Resource.Loading())
                val response = repository.getRepoList(searchQuery)
                Log.i(TAG, "getRepoList() called response: $response")
                searchResponse.postValue(handleSearchResponse(response))
            } catch (ex: Exception) {
                searchResponse.postValue(Resource.Error(ex.localizedMessage ?: ""))
            }
        }
    }

    private fun handleSearchResponse(response: Response<RepoListResponse>): Resource<RepoListResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Log.i(TAG, "handle search response: response: $resultResponse")
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getIssuesList(searchQuery: String) {
        viewModelScope.launch {
            try {
                issueResponse.postValue(Resource.Loading())
                val response = repository.getIssuesInRepo(searchQuery)
                issueResponse.postValue(handleIssueResponse(response))
            } catch (ex: Exception) {
                issueResponse.postValue(Resource.Error(ex.localizedMessage ?: ""))
            }
        }
    }

    private fun handleIssueResponse(response: Response<List<IssueListItem>>): Resource<List<IssueListItem>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}