package com.example.trabalho_commov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho_commov.R
import com.example.trabalho_commov.api.Note


class WebNotasAdapter(val notas: List<Note>): RecyclerView.Adapter<NotasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerline, parent, false)
        return NotasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notas.size
    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        return holder.bind(notas[position])
    }
}

class NotasViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    private val titulo: TextView = itemView.findViewById(R.id.titulo)
    private val descricao:TextView = itemView.findViewById(R.id.descricao)

    fun bind(nota: Note) {
        titulo.text = nota.titulo
        descricao.text = nota.descricao
    }

}
