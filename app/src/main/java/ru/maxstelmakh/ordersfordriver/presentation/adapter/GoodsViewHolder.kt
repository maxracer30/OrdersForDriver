package ru.maxstelmakh.ordersfordriver.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.maxstelmakh.ordersfordriver.data.model.Goods
import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.databinding.GoodsItemBinding
import ru.maxstelmakh.ordersfordriver.databinding.OrderItemBinding

class GoodsViewHolder(private val binding: GoodsItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(goods: Goods) = with(binding) {

        goodsName.text = goods.name
        goodsPrise.text = goods.price.toString()
        goodsQuantity.text = goods.quantity.toString()
        goodsSumm.text = goods.summ.toString()

    }
}