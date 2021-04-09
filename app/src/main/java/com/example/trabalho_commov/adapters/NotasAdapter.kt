package com.example.trabalho_commov.adapters

import android.content.Context
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho_commov.entities.Nota
import com.example.trabalho_commov.R
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import java.nio.file.Files

class NotasAdapter internal constructor(
    context: Context,
    val listener: RowClickListener
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()

    class NotaViewHolder(itemView: View,val listener: RowClickListener) : RecyclerView.ViewHolder(itemView) {
        val NotaItemView: TextView = itemView.findViewById(R.id.textView)
        val deleteNotaID = itemView.deletebutton
        val EditNotaID = itemView.editbutton


        fun bind(nota: Nota){
            deleteNotaID.setOnClickListener{
                listener.onDeleteNotaClickListener(nota)
            }
            EditNotaID.setOnClickListener{
                listener.onItemClick(nota)
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

        val stringtitle: String = holder.itemView.context.getString(R.string.Title)
        val stringdesc: String = holder.itemView.context.getString(R.string.Description)

        holder.NotaItemView.text = stringtitle + " - " + current.titulo + "\n\n" + stringdesc +  " - "  + current.descricao

        holder.bind(current);

    }

    internal fun setNotas(notas: List<Nota>) {
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size


    interface RowClickListener{
        fun onDeleteNotaClickListener(nota: Nota)
        fun onItemClick(nota: Nota)
    }




}