package com.eggdevs.githubvisualizer.ui.issuelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.eggdevs.githubvisualizer.commonviewmodel.GithubViewModel
import com.eggdevs.githubvisualizer.databinding.FragmentIssueListBinding
import com.eggdevs.githubvisualizer.util.Resource
import com.eggdevs.githubvisualizer.util.TAG

class IssueListFragment: Fragment() {
    private lateinit var binding: FragmentIssueListBinding
    private val args: IssueListFragmentArgs by navArgs()
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(GithubViewModel::class.java)
    }
    private val issueAdapter = IssueAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIssueListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.repo.fullName?.let {
            viewModel.getIssuesList(it)
        }
        setUpRecyclerView()
        addObserver()
    }

    private fun addObserver() {
        viewModel.issueResponse.observe(viewLifecycleOwner) { response ->
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
                        issueAdapter.differ.submitList(it.toList())
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.apply {
            rvIssueList.apply {
                setHasFixedSize(true)
                adapter = issueAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun hideProgressBar() {
        binding.pb.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.pb.visibility = View.VISIBLE
    }
}