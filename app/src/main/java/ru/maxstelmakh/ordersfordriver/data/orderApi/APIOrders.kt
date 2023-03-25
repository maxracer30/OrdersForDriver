package ru.maxstelmakh.ordersfordriver.data.orderApi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.maxstelmakh.ordersfordriver.data.model.Order

interface APIOrders {
    @GET("get_order.php")
    fun orders(): Call<List<Order>>

    @POST("set_order.php")
    fun sendChangeOrders(@Body order: Order) {
        TODO()
    }
}
