package ru.maxstelmakh.ordersfordriver.data.orderApi.implusecases

import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.domain.repositories.OrdersRepository
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.GetOrderUseCase
import javax.inject.Inject

class DefaultGetOrder @Inject constructor(
    private val repository: OrdersRepository
): GetOrderUseCase {
    override suspend operator fun invoke(): Result<Order> {
        return repository.fetchOrders()
    }
}