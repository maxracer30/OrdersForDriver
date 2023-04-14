package ru.maxstelmakh.ordersfordriver.presentation.adapter

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.databinding.GoodsItemBinding
import ru.maxstelmakh.ordersfordriver.domain.model.GoodsModel

class GoodsViewHolder(
    private val binding: GoodsItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(goods: Goods, res: Resources, listener: ClickListener<GoodsModel>) = with(binding) {

        goodsName.text = goods.name
        goodsPrise.text = buildString {
            append(goods.price.toString())
            append(res.getString(R.string.perPiece))
        }
        goodsQuantity.text = buildString {
            append(res.getString(R.string.quantity))
            append(goods.quantity.toString())
        }
        goodsSumm.text = buildString {
            append(res.getString(R.string.summary))
            append(goods.summ.toString())
        }

        root.setOnClickListener {
            listener.onClick(goods)
        }
    }
}