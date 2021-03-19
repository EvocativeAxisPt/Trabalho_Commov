package com.example.trabalho_commov.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho_commov.entities.Nota
import com.example.trabalho_commov.R

class NotasAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<NotasAdapter.CityViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cities = emptyList<Nota>() // Cached copy of cities

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return CityViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val current = cities[position]
        holder.cityItemView.text = current.id.toString() + " - " + current.city + "-" + current.country
    }

    internal fun setCities(notas: List<Nota>) {
        this.cities = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = cities.size
}