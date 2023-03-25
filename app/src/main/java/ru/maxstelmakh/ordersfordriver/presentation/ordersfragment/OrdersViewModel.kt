package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.GetOrdersUseCase

class OrdersViewModel(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            handler(getOrdersUseCase())
        }
    }


    private suspend fun handler(result: Result<List<Order>>) = when (result) {
        is Result.Success -> withContext(Dispatchers.Main) {
            _orders.value = result.data
        }

        is Result.Failure -> withContext(Dispatchers.Main) {
            _orders.value = emptyList()
        }
    }

}