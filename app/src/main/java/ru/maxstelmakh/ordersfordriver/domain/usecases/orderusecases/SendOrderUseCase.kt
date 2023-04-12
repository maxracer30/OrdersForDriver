package ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases

import okhttp3.ResponseBody
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.ResponseOrder

interface SendOrderUseCase {
    suspend operator fun invoke(order: ResponseOrder): Result<ResponseBody>
}