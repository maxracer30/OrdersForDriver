package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.ResponseGoods
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.ResponseOrder
import ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.model.repsonse.LinkToDownload
import ru.maxstelmakh.ordersfordriver.domain.model.GoodsToChange
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.GetOrderUseCase
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.SendOrderUseCase
import ru.maxstelmakh.ordersfordriver.domain.usecases.photousecases.DeletePhotoUseCase
import ru.maxstelmakh.ordersfordriver.domain.usecases.remotephotousecases.UploadToRemoteUseCase
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrderUseCase: GetOrderUseCase,
    private val uploadToRemoteUseCase: UploadToRemoteUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val sendOrderUseCase: SendOrderUseCase
) : ViewModel() {

    private val _order = MutableSharedFlow<List<Order>>()
    val order: SharedFlow<List<Order>> = _order

    private var _originalOrder: Order? = null
    private val originalOrder: Order get() = _originalOrder!!

    private var _changedOrder: Order? = null
    private val changedOrder: Order get() = _changedOrder!!

    private var _changedGoods: GoodsToChange? = null
    val changedGoods: GoodsToChange get() = _changedGoods!!

    private var _changeGoodsReasons = HashMap<Long, String>()
    val changeGoodsReasons get() = _changeGoodsReasons

    private var _responseOrder: ResponseOrder? = null
    private val responseOrder: ResponseOrder get() = _responseOrder!!

    init {
        getNewOrder()
    }

    private fun getNewOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            getOrderHandler(getOrderUseCase())
        }
    }

    private suspend fun getOrderHandler(result: Result<Order>) = when (result) {
        is Result.Success -> withContext(Dispatchers.Main) {
            result.data.let {
                _originalOrder = it
                _changedOrder = it

                _order.emit(listOf(it))
            }
        }

        is Result.Failure -> withContext(Dispatchers.Main) {
            _order.emit(emptyList())
            getNewOrder()
        }
    }

    fun saveGoods() {
        viewModelScope.launch(Dispatchers.IO) {
            val changedGoodsPosition =
                changedOrder.goods?.let { list ->
                    list.indexOfFirst { it.article == changedGoods.item?.article }
                }!!

            when (changedGoods.item) {
                changedOrder.goods!![changedGoodsPosition] -> {
                    return@launch
                }
                else -> {
                    val changedList: List<Goods> = changedOrder.goods?.toMutableList()!!.apply {
                        this[changedGoodsPosition] = changedGoods.item!!
                    }

                    _changeGoodsReasons[changedGoods.item!!.article] = changedGoods.changeReason!!

                    _changedOrder = originalOrder.copy(goods = changedList)

                    _order.emit(listOf(changedOrder))
                }
            }
        }
    }

    fun completeOrder() {
        when (originalOrder.goods == changedOrder.goods) {
            true -> {
                _responseOrder = ResponseOrder(
                    orderId = originalOrder.orderId,
                    orderDate = originalOrder.orderDate,
                    orderNum = originalOrder.orderNum,
                    phone = originalOrder.phone,
                    goods = emptyList()
                )

                viewModelScope.launch(Dispatchers.IO) {
                    sendOrder()
                }
            }
            false -> {

                val changedGoodsList = changedOrder.goods?.toMutableList()

                changedGoodsList?.removeIf { changedGoods ->
                    val article = changedGoods.article
                    val originalThisGoods = originalOrder.goods?.let { list ->
                        return@let list[list.indexOfFirst { it.article == article }]
                    }
                    changedGoods.quantity == originalThisGoods?.quantity
                }

                _changedOrder = changedOrder.copy(
                    goods = changedGoodsList
                )

                uploadToRemote()
            }
        }
    }

    private fun uploadToRemote() {
        viewModelScope.launch(Dispatchers.IO) {
            changedOrder.goods?.first()?.let {
                uploadHandler(uploadToRemoteUseCase(it.article.toString()))
            }
        }
    }


    private suspend fun sendOrder() {
        sendOrderHandler(sendOrderUseCase(responseOrder))
    }

    private suspend fun sendOrderHandler(result: Result<ResponseBody>) = when (result) {
        is Result.Success -> withContext(Dispatchers.Main) {
            clearResources()
            getNewOrder()
        }
        is Result.Failure -> withContext(Dispatchers.Main) {
            sendOrder()
        }
    }


    private suspend fun uploadHandler(result: Result<LinkToDownload>) = when (result) {
        is Result.Success -> {
            preparationOrderResponse(result.data.href)
            removeSentGoods()
            when (changedOrder.goods.isNullOrEmpty()) {
                true -> withContext(Dispatchers.IO) {
                    sendOrder()
                }
                false -> {
                    uploadToRemote()
                }
            }
        }
        is Result.Failure -> {
            uploadToRemote()
        }
    }

    private fun clearResources() {
        viewModelScope.launch {
            _order.emit(emptyList())

            responseOrder.goods?.forEach {
                println(deletePhotoUseCase(it.article.toString()))
            }
            _changeGoodsReasons.clear()
            _changedOrder = null
            _originalOrder = null
            _changedGoods = null
            _responseOrder = null

        }

    }

    private fun removeSentGoods() {
        val unsentPhoto = changedOrder.goods?.toMutableList()

        unsentPhoto?.removeFirst()
        _changedOrder = changedOrder.copy(
            goods = unsentPhoto
        )
    }

    private fun preparationOrderResponse(href: String?) {

        val listResponseGoods = mutableListOf<ResponseGoods>()
        if (_responseOrder != null) {
            responseOrder.goods?.let { listResponseGoods.addAll(it) }
        }

        changedOrder.goods?.first()?.let {
            listResponseGoods.add(
                ResponseGoods(
                    article = it.article,
                    name = it.name,
                    quantity = it.quantity,
                    price = it.price,
                    summ = it.summ,
                    reason = changeGoodsReasons[it.article].toString(),
                    photoLink = href.toString()
                )
            )

            _responseOrder = ResponseOrder(
                orderId = originalOrder.orderId,
                orderDate = originalOrder.orderDate,
                orderNum = originalOrder.orderNum,
                phone = originalOrder.phone,
                goods = listResponseGoods
            )
        }
    }

    fun setChangedGoods(goodsToChange: GoodsToChange) {
        _changedGoods = goodsToChange
    }

    fun getOriginalGoods(): Goods {
        val changedGoodsPosition =
            changedOrder.goods?.let { list ->
                list.indexOfFirst { it.article == changedGoods.item?.article }
            }!!

        return originalOrder.goods!![changedGoodsPosition]
    }
}
