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
import com.example.mti_p21_pokebattle.models.PokedexPokemonDetail


class PokedexAdapter(
    val data: List<PokedexPokemonDetail>,
    val context: Context?,
    val onItemClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<PokedexAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.list_item_pokedex_txt_name)
        val typeImgView1: ImageView = itemView.findViewById(R.id.list_item_pokedex_type1_img)
        val typeImgView2: ImageView = itemView.findViewById(R.id.list_item_pokedex_type2_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView: View =
            LayoutInflater.from(context).inflate(R.layout.list_item_pokedex, parent, false)
        rowView.setOnClickListener(onItemClickListener)
        return ViewHolder(
            rowView
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = data[position].name.capitalize()
        if (data[position].types.size == 2) {
            holder.typeImgView1.setImageResource(getType(data[position].types[0].name))
            holder.typeImgView2.setImageResource(getType(data[position].types[1].name))
        } else {
            holder.typeImgView1.setImageResource(getType(data[position].types[0].name))
            holder.typeImgView2.setImageResource(android.R.color.transparent)
        }
        holder.itemView.tag = data[position]
    }
}