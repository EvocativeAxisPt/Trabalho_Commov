package com.example.trabalho_commov.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho_commov.entities.Nota
import com.example.trabalho_commov.R
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class NotasAdapter internal constructor(
    context: Context,
    val listener: RowClickListener
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()

    class NotaViewHolder(itemView: View,val listener: RowClickListener) : RecyclerView.ViewHolder(itemView) {
        val NotaItemView: TextView = itemView.findViewById(R.id.textView)
        val deleteUserID = itemView.deletebutton



        fun bind(nota: Nota){
            deleteUserID.setOnClickListener{
                listener.onDeleteNotaClickListener(nota)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotaViewHolder(
            itemView, listener
        )
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.NotaItemView.text = current.id.toString() + " - " + current.titulo + "\n" + current.descricao

        holder.bind(current);

    }

    internal fun setNotas(notas: List<Nota>) {
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size


    interface RowClickListener{
        fun onDeleteNotaClickListener(nota: Nota)
    }

    interface onItemClickListener{
        fun onItemClick(nota: Nota)
    }


}