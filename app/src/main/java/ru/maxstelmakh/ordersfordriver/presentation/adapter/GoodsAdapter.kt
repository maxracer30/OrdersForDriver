package ru.maxstelmakh.ordersfordriver.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.data.model.Goods
import ru.maxstelmakh.ordersfordriver.databinding.GoodsItemBinding

class GoodsAdapter : RecyclerView.Adapter<GoodsAdapter.GoodsHolder>() {

    private var goods = emptyList<Goods>()

    class GoodsHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goods_item, parent, false)
        return GoodsHolder(view)
    }

    override fun onBindViewHolder(holder: GoodsHolder, position: Int) {

    }


    override fun getItemCount() = goods.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(goodsList: List<Goods>) {
        goods = goodsList
        notifyDataSetChanged()
    }
}
