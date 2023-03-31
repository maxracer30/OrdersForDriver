package ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases

import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.domain.repository.OrdersRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrdersRepository
) {
    suspend operator fun invoke(): Result<Order> {
        return repository.fetchOrders()
    }
}