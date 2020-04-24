package com.example.mti_p21_pokebattle.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mti_p21_pokebattle.R
import com.example.mti_p21_pokebattle.getType
import com.example.mti_p21_pokebattle.models.Move


class MoveAdapter(
    var data: MutableList<Move>,
    val context: Context?,
    val onItemClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<MoveAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moveTypeView: ImageView = itemView.findViewById(R.id.moveTypeImage)
        val nameTextView: TextView = itemView.findViewById(R.id.moveName)
        val powerTextView: TextView = itemView.findViewById(R.id.movePower)
        val accuracyTextView: TextView = itemView.findViewById(R.id.moveAccuracy)
        val classTextView: TextView = itemView.findViewById(R.id.moveClass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView: View =
            LayoutInflater.from(context).inflate(R.layout.list_item_move, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(
            rowView
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = data[position].name
        holder.powerTextView.text = data[position].power.toString()
        holder.accuracyTextView.text = data[position].accuracy.toString()
        holder.classTextView.text = data[position].damage_class.name
        holder.moveTypeView.setImageResource(getType(data[position].type.name))
        holder.itemView.tag = data[position]
    }

    fun setMoves(moves: MutableList<Move>) {
        data = moves;
        notifyDataSetChanged();
    }
}