package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.domain.model.GoodsToChange
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.GetOrderUseCase
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrderUseCase: GetOrderUseCase,
    private val repository: PictureRepository
) : ViewModel() {

    private val _order = MutableSharedFlow<List<Order>>()
    val order: SharedFlow<List<Order>> = _order

    lateinit var originalOrder: Order
    lateinit var changedOrder: Order

    lateinit var changedGoods: GoodsToChange

    var changeGoodsReasons = HashMap<Long, String>()

    init {
        getNewOrder()
    }

    private fun getNewOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            handler(getOrderUseCase())
        }
    }

    private suspend fun handler(result: Result<Order>) = when (result) {
        is Result.Success -> withContext(Dispatchers.Main) {
            result.data.let {
                originalOrder = it
                changedOrder = it

                _order.emit(listOf(it))
            }
        }

        is Result.Failure -> withContext(Dispatchers.Main) {
            _order.emit(emptyList())
            getNewOrder()
        }
    }

    fun saveGoods() {
        viewModelScope.launch(Dispatchers.IO) {
            val changedGoodsPosition =
                changedOrder.goods?.let { list ->
                    list.indexOfFirst { it.article == changedGoods.item.article }
                }!!

            when (changedGoods.item) {
                changedOrder.goods!![changedGoodsPosition] -> return@launch
                else -> {
                    val changedList: List<Goods> = changedOrder.goods?.toMutableList()!!.apply {
                        this[changedGoodsPosition] = changedGoods.item
                    }

                    changeGoodsReasons[changedGoods.item.article] = changedGoods.changeReason

                    changedOrder = originalOrder.copy(goods = changedList)

                    _order.emit(listOf(changedOrder))
                }
            }
        }
    }

    fun completeOrder() {
        when (originalOrder.goods == changedOrder.goods) {
            true -> {}
            false -> {}
        }
    }

    fun getOriginalGoods(): Goods {
        val changedGoodsPosition =
            changedOrder.goods?.let { list ->
                list.indexOfFirst { it.article == changedGoods.item.article }
            }!!

        return originalOrder.goods!![changedGoodsPosition]
    }
}