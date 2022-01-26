package com.example.scopehomework.presentation.feature.userlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.scopehomework.R
import com.example.scopehomework.databinding.FragmentUserListBinding
import com.example.scopehomework.domain.feature.vehiclelocation.entity.User
import com.example.scopehomework.presentation.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserListFragment : Fragment(), UserRecyclerViewAdapter.UserClickListener {

    private lateinit var binding: FragmentUserListBinding
    private lateinit var mAdapter: UserRecyclerViewAdapter
    private val viewModel: UserListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)
        mAdapter = UserRecyclerViewAdapter(this)
        binding.userList.adapter = mAdapter
        binding.userList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        getUserList()
        return binding.root
    }

    private fun showErrorDialog(context: Context) {
        val dialog = MaterialDialog(context)
            .title(R.string.dialog_title)
            .message(R.string.dialog_message)
        dialog.show {
            positiveButton(R.string.dialog_positive_button) {
                getUserList()
            }
            negativeButton(R.string.dialog_negative_button)
        }
    }

    private fun getUserList() {
        lifecycleScope.launch {
            viewModel.getUserListResponse()
                .collect {
                    when (it) {
                        is State.DataState -> mAdapter.data = it.data
                        is State.ErrorState -> {
                            context?.let { context ->
                                showErrorDialog(context)
                            }
                        }
                        is State.LoadingState -> {
                            //Add behaviour if needed while API call is loading
                        }
                    }
                }
        }
    }


    override fun onUserClicked(user: User) {
        findNavController().navigate(UserListFragmentDirections.toLocationFragment(user))
    }
}