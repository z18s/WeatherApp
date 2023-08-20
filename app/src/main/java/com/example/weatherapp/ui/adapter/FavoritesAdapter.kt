package com.example.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ItemFavoritesBinding
import com.example.weatherapp.mvp.presenter.IFavoritesPresenter
import com.example.weatherapp.mvp.view.IFavoritesItemView

class FavoritesAdapter(private val presenter: IFavoritesPresenter) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { presenter.onItemClick(holder) }
        presenter.bindView(holder)
    }

    override fun getItemCount(): Int = presenter.getCount()

    class ViewHolder(private val binding: ItemFavoritesBinding) : RecyclerView.ViewHolder(binding.root), IFavoritesItemView {

        override fun setCity(cityName: String) {
            binding.tvFavoriteCity.text = cityName
        }

        override fun setCountry(countryName: String) {
            binding.tvFavoriteCountry.text = countryName
        }

        override fun getPos(): Int = adapterPosition
    }
}