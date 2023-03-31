package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.GetOrdersUseCase
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _order = MutableSharedFlow<List<Order>>()
    val order: SharedFlow<List<Order>> = _order

    lateinit var changedOrder: Order

    init {
        getNewOrder()
    }
    private fun getNewOrder(){
        viewModelScope.launch(Dispatchers.IO) {
            handler(getOrdersUseCase())
        }
    }

    private suspend fun handler(result: Result<Order>) = when (result) {
        is Result.Success -> withContext(Dispatchers.Main) {
            changedOrder = result.data
            _order.emit(listOf( result.data))
        }

        is Result.Failure -> withContext(Dispatchers.Main) {
            _order.emit(emptyList())
            getNewOrder()
        }
    }
}