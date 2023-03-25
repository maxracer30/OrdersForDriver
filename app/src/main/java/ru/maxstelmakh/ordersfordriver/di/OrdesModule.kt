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
import ru.maxstelmakh.ordersfordriver.data.orderApi.APIOrders
import ru.maxstelmakh.ordersfordriver.data.orderApi.OrdersRepository

@Module(includes = [BindLocationModule::class])
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
            .baseUrl("http://vseotlichno.com/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIOrders::class.java)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
interface BindLocationModule {

    @Binds
    fun bindLocationRepository(repository: OrdersRepository.Base): OrdersRepository
}
