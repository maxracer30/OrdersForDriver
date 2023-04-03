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
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.GetOrderUseCase
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrderUseCase: GetOrderUseCase
) : ViewModel() {

    private val _order = MutableSharedFlow<List<Order>>()
    val order: SharedFlow<List<Order>> = _order

    private lateinit var originalOrder: Order
    private lateinit var changedOrder: Order

    lateinit var changedGoods: Goods

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

    fun getOriginalGoods(): Goods {
        val changedGoodsPosition =
            changedOrder.goods?.let { list ->
                list.indexOfFirst { it.article == changedGoods.article }
            }!!

        return originalOrder.goods!![changedGoodsPosition]
    }

    fun saveGoods() {

        viewModelScope.launch(Dispatchers.IO) {

            val changedGoodsPosition =
                changedOrder.goods?.let { list ->
                    list.indexOfFirst { it.article == changedGoods.article }
                }!!



            when (changedGoods) {

                changedOrder.goods!![changedGoodsPosition] -> return@launch

                else -> {

                    val changedList: List<Goods> = changedOrder.goods?.toMutableList()!!.apply {
                        this[changedGoodsPosition] = changedGoods
                    }

                    changedOrder = originalOrder.copy(
                        goods = changedList
                    )

                    _order.emit(listOf(changedOrder))
                }
            }
        }
    }
}