package ru.maxstelmakh.ordersfordriver.presentation.changegoodsfragment

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
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

    private var _originalGoods: Goods? = null
    val originalGoods: Goods get() = _originalGoods!!

    private val _changedGoods = MutableStateFlow(GoodsToChange())
    var changedGoods: StateFlow<GoodsToChange> = _changedGoods

    private val _photo = MutableSharedFlow<Bitmap>()
    val photo: SharedFlow<Bitmap> = _photo

    private val _checkPhoto = MutableStateFlow(false)
    val checkPhoto: StateFlow<Boolean> = _checkPhoto

    fun loadPhoto(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadPhoto(name).let {
                when (it) {
                    null -> _checkPhoto.value = false
                    else -> {
                        _checkPhoto.value = true
                        _photo.emit(it)
                    }
                }
            }
        }
    }

    fun savePhoto(name: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.savePhoto(name = name, bitmap = bitmap)
        }
        loadPhoto(name)
    }

    fun setCount(newCount: Int) {
        _changedGoods.value = changedGoods.value.copy(
            item = changedGoods.value.item?.copy(
                quantity = newCount,
                summ = (originalGoods.price * newCount * 100.0).roundToInt() / 100.00
            )
        )
    }

    fun setReason(newReason: String) {
        _changedGoods.value = changedGoods.value.copy(
            changeReason = newReason
        )
    }

    fun checkCount(): Boolean = originalGoods.quantity == changedGoods.value.item?.quantity

    fun setData(original: Goods, changed: GoodsToChange) {
        _originalGoods = original
        _changedGoods.value = changed
    }
}