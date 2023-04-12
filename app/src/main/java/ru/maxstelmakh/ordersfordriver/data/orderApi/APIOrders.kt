package ru.maxstelmakh.ordersfordriver.data.orderApi

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.ResponseOrder

interface APIOrders {
    @GET("get_order.php")
    suspend fun order(): Response<Order>

    @POST("set_order.php")
    suspend fun orderComplete(
        @Body order: ResponseOrder,
    ): Response<ResponseBody>
}