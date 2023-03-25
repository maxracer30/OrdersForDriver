package ru.maxstelmakh.ordersfordriver.data.orderApi

sealed class Result<T : Any> {
    class Success<T : Any>(val data: T) : Result<T>()
    class Failure<T : Any>(val statusCode: Int, message: String? = null) : Result<T>()
}