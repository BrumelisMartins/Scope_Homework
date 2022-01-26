package com.example.scopehomework.presentation.feature.userlist

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.scopehomework.R
import com.example.scopehomework.databinding.UserListItemBinding
import com.example.scopehomework.domain.feature.vehiclelocation.entity.User


class UserRecyclerViewAdapter(
    private val userClickListener: UserClickListener
) : RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder>() {

    var data = listOf<User>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: UserListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.user_list_item, parent, false)
        return UserViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            userClickListener.onUserClicked(item)
        }

    }

    override fun getItemCount(): Int = data.size


    inner class UserViewHolder(val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                userImage.load(user.owner?.foto)
                name.text = user.owner?.name
                surname.text = user.owner?.surname
            }
        }
    }

    interface UserClickListener {
        fun onUserClicked(user: User)
    }

}