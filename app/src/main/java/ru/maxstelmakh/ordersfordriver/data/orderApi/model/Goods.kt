package ru.maxstelmakh.ordersfordriver.data.orderApi.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import ru.maxstelmakh.ordersfordriver.domain.model.GoodsModel

@Parcelize
data class Goods(
    @SerializedName("article") val article: Long,
    @SerializedName("name") val name: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("summ") val summ: Double,
) : GoodsModel, Parcelable