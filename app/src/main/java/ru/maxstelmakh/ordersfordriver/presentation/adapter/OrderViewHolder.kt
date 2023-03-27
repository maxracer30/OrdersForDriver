package ru.maxstelmakh.ordersfordriver.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.databinding.OrderItemBinding

class OrderViewHolder(private val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(order: Order) = with(binding) {

        orderNum.text = order.orderNum
        orderDate.text = order.orderDate

    }
}