package ru.maxstelmakh.ordersfordriver.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.databinding.OrderItemBinding

class OrderAdapter : ListAdapter<Order, OrderAdapter.Holder>(Comparator()) {
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = OrderItemBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(order: Order) = with(binding) {
            orderNum.text = order.order_num
            orderDate.text = order.order_date
        }
    }

    class Comparator : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.order_id == newItem.order_id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}