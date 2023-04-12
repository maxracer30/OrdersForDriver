package ru.maxstelmakh.ordersfordriver.presentation.adapter

import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods

interface GoodsClickListener {
    fun onClick(goods: Goods) {
    }
}
