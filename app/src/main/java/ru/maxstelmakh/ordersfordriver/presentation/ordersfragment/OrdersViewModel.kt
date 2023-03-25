package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.GetOrdersUseCase
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    init {
        println("s")
        println("s")
        viewModelScope.launch(Dispatchers.IO) {
            println("Мы во вьюмодели")
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