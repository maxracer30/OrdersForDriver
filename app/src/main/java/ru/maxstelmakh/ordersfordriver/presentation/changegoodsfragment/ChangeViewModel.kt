package ru.maxstelmakh.ordersfordriver.presentation.changegoodsfragment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ChangeViewModel @Inject constructor() : ViewModel() {

    lateinit var originalGoods: Goods
    lateinit var changedGoods: Goods
    var newCount: Int = 0

    fun setChangedGoods() {
        changedGoods = originalGoods.copy(
            quantity = newCount,
            summ = (originalGoods.price * newCount * 100.0).roundToInt() / 100.00
        )
    }
}
