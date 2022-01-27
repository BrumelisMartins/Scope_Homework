package com.example.scopehomework.presentation.feature.vehiclelocation

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import coil.load
import coil.request.ImageRequest
import com.example.scopehomework.R
import com.example.scopehomework.databinding.VehicleInfoItemBinding


class VehicleInfoRecyclerViewAdapter : RecyclerView.Adapter<VehicleInfoRecyclerViewAdapter.VehicleInfoViewHolder>() {

    var data = listOf<VehicleData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleInfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: VehicleInfoItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.vehicle_info_item, parent, false)
        return VehicleInfoViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: VehicleInfoViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = data.size


    inner class VehicleInfoViewHolder(private val binding: VehicleInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(vehicle: VehicleData) {
            with(binding) {
                vehicleTitle.text = vehicle.name
                currentAddress.text = vehicle.address
                colorImage.setColorFilter(Color.parseColor(vehicle.color))
                carImage.load(vehicle.image) {
                    listener(
                        onError = { _: ImageRequest, _: Throwable ->
                            carImage.visibility = View.GONE
                        }
                    )
                }
            }
        }
    }


}