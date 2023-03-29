package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.di.OrdersForDriver
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.GetOrdersUseCase
import ru.maxstelmakh.ordersfordriver.presentation.MainActivity
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _order = MutableSharedFlow<Order>()
    val order: SharedFlow<Order> = _order

    fun getNewOrder(){
        viewModelScope.launch(Dispatchers.IO) {
            handler(getOrdersUseCase())
        }
    }

    private suspend fun handler(result: Result<Order>) = when (result) {
        is Result.Success -> withContext(Dispatchers.Main) {
            _order.emit(result.data)
        }

        is Result.Failure -> withContext(Dispatchers.Main) {
        }
    }
}