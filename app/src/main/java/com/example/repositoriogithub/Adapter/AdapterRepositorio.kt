package com.example.repositoriogithub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.repositoriogithub.Model.Item
import com.example.repositoriogithub.R
import com.squareup.picasso.Picasso

class AdapterRepositorio : RecyclerView.Adapter<AdapterRepositorio.Repositorio>() {

    var listBebidas: ArrayList<Item> = arrayListOf()

    fun adicionarRepositoriosLista(itens: List<Item>) {
        listBebidas.clear();
        listBebidas.addAll(itens)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Repositorio {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_repositorio, parent, false)
        val holder = Repositorio(view)
        return holder
    }

    override fun onBindViewHolder(holder: Repositorio, position: Int) {
        holder.bind(listBebidas[position])
    }

    override fun getItemCount(): Int {
        return listBebidas!!.size
    }

    class Repositorio(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_nome_autor = itemView.findViewById<TextView>(R.id.txt_nome_autor)
        val txt_nome_repositorio = itemView.findViewById<TextView>(R.id.txt_repositorio)
        val txt_qtd_forks = itemView.findViewById<TextView>(R.id.txt_qtd_fork)
        val img_autor_repositorio = itemView.findViewById<ImageView>(R.id.img_foto_git)
        val txt_qtd_estrelas_repositorio =
            itemView.findViewById<TextView>(R.id.txt_star_repositorio)

        fun bind(repositorio: Item) {
            txt_nome_autor.text = repositorio.owner.login
            txt_nome_repositorio.text = repositorio.fullName
            txt_qtd_forks.text = repositorio.forks.toString()
            txt_qtd_estrelas_repositorio.text = repositorio.stargazersCount.toString()

            Picasso.get()
                .load(repositorio.owner.avatarUrl)
                .resize(180, 180)
                .centerCrop()
                .error(R.drawable.image_error)
                .into(img_autor_repositorio)
        }
    }
}