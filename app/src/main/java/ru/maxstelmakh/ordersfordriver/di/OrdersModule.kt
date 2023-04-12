package ru.maxstelmakh.ordersfordriver.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.maxstelmakh.ordersfordriver.BuildConfig
import ru.maxstelmakh.ordersfordriver.data.orderApi.APIOrders
import ru.maxstelmakh.ordersfordriver.data.orderApi.BaseRepository
import ru.maxstelmakh.ordersfordriver.data.orderApi.implusecases.DefaultGetOrder
import ru.maxstelmakh.ordersfordriver.data.orderApi.implusecases.DefaultSendOrder
import ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.APIPhoto
import ru.maxstelmakh.ordersfordriver.domain.repositories.OrdersRepository
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.GetOrderUseCase
import ru.maxstelmakh.ordersfordriver.domain.usecases.orderusecases.SendOrderUseCase

@Module(
    includes = [
        BindOrdersModule::class,
        PictureModule::class,
        PictureRemoteModule::class,
    ]
)
@InstallIn(ViewModelComponent::class)
class OrdersModule {

    @Provides
    fun interceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun okHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor = interceptor)
            .build()
    }

    @Provides
    fun provideAPIService(okHttpClient: OkHttpClient): APIOrders {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIOrders::class.java)
    }

    @Provides
    fun provideAPIPhotoService(okHttpClient: OkHttpClient): APIPhoto {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.PICTURES_STORAGE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIPhoto::class.java)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
interface BindOrdersModule {

    @Binds
    fun bindOrdersRepository(repository: BaseRepository): OrdersRepository

    @Binds
    fun provideNewOrder(getOrder: DefaultGetOrder): GetOrderUseCase

    @Binds
    fun provideSendOrder(sendOrder: DefaultSendOrder): SendOrderUseCase
}
