package ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases

import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order


interface GetOrderUseCase {
    suspend operator fun invoke(): Result<Order>
}