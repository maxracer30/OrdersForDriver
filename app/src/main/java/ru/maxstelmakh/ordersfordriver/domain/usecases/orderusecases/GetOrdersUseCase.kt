package ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases

import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.domain.repository.OrdersRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrdersRepository
) {
    suspend operator fun invoke(): Result<List<Order>> {
        return repository.fetchOrders()
    }
}