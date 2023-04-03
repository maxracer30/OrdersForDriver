package ru.maxstelmakh.ordersfordriver.presentation.changegoodsfragment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ChangeViewModel @Inject constructor(
    private val pictureRepository: PictureRepository
) : ViewModel() {

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
