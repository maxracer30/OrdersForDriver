package ru.maxstelmakh.ordersfordriver.data.orderApi.implusecases

import okhttp3.ResponseBody
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.ResponseOrder
import ru.maxstelmakh.ordersfordriver.domain.repositories.OrdersRepository
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.SendOrderUseCase
import javax.inject.Inject

class DefaultSendOrder @Inject constructor(
    private val repository: OrdersRepository
) : SendOrderUseCase {
    override suspend fun invoke(order: ResponseOrder): Result<ResponseBody> {
        return repository.sendOrder(order = order)
    }
}