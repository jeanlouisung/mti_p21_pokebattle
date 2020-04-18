package com.example.mti_p21_pokebattle.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mti_p21_pokebattle.R
import com.example.mti_p21_pokebattle.models.PokemonDetail


class PokedexAdapter(val data : List<PokemonDetail>, val context : Context?) : RecyclerView.Adapter<PokedexAdapter.ViewHolder>() {

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView : TextView = itemView.findViewById(R.id.list_item_pokedex_txt_name)
        val imgView : ImageView = itemView.findViewById(R.id.list_item_pokedex_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView : View = LayoutInflater.from(context).inflate(R.layout.list_item_pokedex, parent, false)

        return ViewHolder(
            rowView
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = data[position].name
        holder.imgView.setImageURI(null)

        Log.d("Pokemon", data[position].sprite)
        val uri: Uri = Uri.parse(data[position].sprite)
//        holder.imgView.setImageURI(Uri.parse(data[position].sprite))
//        holder.imgView.setImageResource(android.R.drawable.arrow_up_float)
    }
}