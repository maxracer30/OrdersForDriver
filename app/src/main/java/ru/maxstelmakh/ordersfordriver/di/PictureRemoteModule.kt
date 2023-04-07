package ru.maxstelmakh.ordersfordriver.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.DefaultApiPhoto
import ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.implusecases.DefaultUploadToRemote
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRemoteRepository
import ru.maxstelmakh.ordersfordriver.domain.usecases.remotephotousecases.UploadToRemoteUseCase

@Module
@InstallIn(ViewModelComponent::class)
interface PictureRemoteModule {

    @Binds
    fun remoteRepository(repository: DefaultApiPhoto): PictureRemoteRepository

    @Binds
    fun bindUpload(impl: DefaultUploadToRemote): UploadToRemoteUseCase
}