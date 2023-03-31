package ru.maxstelmakh.ordersfordriver.presentation.goodsfragment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import javax.inject.Inject

class GoodsViewModel : ViewModel() {

    lateinit var order: Order

}