package ru.maxstelmakh.ordersfordriver.domain.model

import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods

data class GoodsToChange(
    var item: Goods,
    var changeReason: String,
) {
}