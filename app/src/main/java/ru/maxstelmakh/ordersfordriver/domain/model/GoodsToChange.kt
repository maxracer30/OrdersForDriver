package ru.maxstelmakh.ordersfordriver.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods

@Parcelize
data class GoodsToChange(
    var item: Goods? = null,
    var changeReason: String? = null,
) : GoodsModel, Parcelable