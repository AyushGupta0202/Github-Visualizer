package com.eggdevs.githubvisualizer.ui.repolist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eggdevs.githubvisualizer.commonviewmodel.GithubViewModel
import com.eggdevs.githubvisualizer.databinding.FragmentRepoListBinding
import com.eggdevs.githubvisualizer.util.Resource
import com.eggdevs.githubvisualizer.util.SEARCH_REPO_TIME_DELAY
import com.eggdevs.githubvisualizer.util.TAG
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RepoListFragment: Fragment() {
    private val repoAdapter = RepoAdapter()
    private lateinit var binding: FragmentRepoListBinding
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(GithubViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        addObserver()
        addTextWatcher()
        setOnRepoItemClicked()
    }

    private fun addTextWatcher() {
        var job: Job? = null
        binding.apply {
            etSearch.addTextChangedListener { editable ->
                job?.cancel()
                if (editable.isNullOrEmpty().not()) {
                    job = MainScope().launch {
                        delay(SEARCH_REPO_TIME_DELAY)
                        viewModel.getRepoList(editable.toString().trim())
                    }
                }
            }
        }
    }

    private fun addObserver() {
        viewModel.searchResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "An error occurred: ${response.message}")
                    hideProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        repoAdapter.differ.submitList(it.repoListItems.sortedByDescending { listItem -> listItem.stargazersCount }.toList())
                    }
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.pb.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.pb.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        binding.apply {
            rvRepoList.apply {
                setHasFixedSize(true)
                adapter = repoAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun setOnRepoItemClicked() {
        repoAdapter.setOnRepoItemClickListener { repoListItem ->
            findNavController().navigate(
                RepoListFragmentDirections.actionRepoListFragmentToIssueListFragment(repoListItem)
            )
        }
    }
}