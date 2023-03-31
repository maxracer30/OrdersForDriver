package ru.maxstelmakh.ordersfordriver.data.orderApi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order

interface APIOrders {
    @GET("get_order.php")
    fun order(): Call<Order>

    @POST("set_order.php")
    fun sendChangeOrders(@Body order: Order) {
        TODO()
    }
}
