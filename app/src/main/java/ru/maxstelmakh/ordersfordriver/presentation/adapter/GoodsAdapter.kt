package ru.maxstelmakh.ordersfordriver.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.databinding.GoodsItemBinding

class GoodsAdapter(
    private val goodsClickListener: GoodsClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var goods = emptyList<Goods>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {
        val itemBinding =
            GoodsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoodsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GoodsViewHolder)
            .bind(goods = goods[position], holder.itemView.resources, goodsClickListener)
    }

    override fun getItemCount() = goods.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(goodsList: List<Goods>) {
        goods = goodsList
        notifyDataSetChanged()
    }
}
