package com.example.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.mvp.presenter.IFavoritesPresenter
import com.example.weatherapp.mvp.view.IFavoritesItemView

class FavoritesAdapter(private val presenter: IFavoritesPresenter) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorites, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { presenter.onItemClick(holder) }
        presenter.bindView(holder)
    }

    override fun getItemCount(): Int = presenter.getCount()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), IFavoritesItemView {

        private var tvCity: TextView = itemView.findViewById(R.id.tv_favorite_city)
        private var tvCountry: TextView = itemView.findViewById(R.id.tv_favorite_country)

        override fun setCity(cityName: String) {
            tvCity.text = cityName
        }

        override fun setCountry(countryName: String) {
            tvCountry.text = countryName
        }

        override fun getPos(): Int = adapterPosition
    }
}