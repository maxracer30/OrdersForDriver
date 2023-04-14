package ru.maxstelmakh.ordersfordriver.presentation.changegoodsfragment

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.domain.model.GoodsToChange
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ChangeViewModel @Inject constructor(
    private val repository: PictureRepository,
) : ViewModel() {

    lateinit var originalGoods: Goods

    lateinit var changedGoods: GoodsToChange

    var newCount: Int = 0

    var checkHavePhoto = false

    private val _photo = MutableSharedFlow<Bitmap>()
    val photo: SharedFlow<Bitmap> = _photo

    fun loadPhoto(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadPhoto(name).let {
                when (it) {
                    null -> checkHavePhoto = false
                    else -> {
                        checkHavePhoto = true
                        _photo.emit(it)
                    }
                }
            }
        }
    }

    fun savePhoto(name: String, bitmap: Bitmap): Boolean {
        var result = false
        viewModelScope.launch {
            result = repository.savePhoto(name = name, bitmap = bitmap)
        }
        loadPhoto(name)
        return result
    }

    fun setChangedGoods() {
        changedGoods.item = originalGoods.copy(
            quantity = newCount,
            summ = (originalGoods.price * newCount * 100.0).roundToInt() / 100.00
        )
    }

    fun setData(original: Goods, changed: GoodsToChange) {
        originalGoods = original
        changedGoods = changed
    }
}
