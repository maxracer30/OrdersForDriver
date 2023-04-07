package ru.maxstelmakh.ordersfordriver.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.maxstelmakh.ordersfordriver.data.pictureRepository.DefaultPictureRepository
import ru.maxstelmakh.ordersfordriver.data.pictureRepository.implusecases.DefaultDeletePhoto
import ru.maxstelmakh.ordersfordriver.data.pictureRepository.implusecases.DefaultLoadPhoto
import ru.maxstelmakh.ordersfordriver.data.pictureRepository.implusecases.DefaultSavePhoto
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository
import ru.maxstelmakh.ordersfordriver.domain.usecases.photousecases.DeletePhotoUseCase
import ru.maxstelmakh.ordersfordriver.domain.usecases.photousecases.LoadPhotoUseCase
import ru.maxstelmakh.ordersfordriver.domain.usecases.photousecases.SavePhotoUseCase

@Module
@InstallIn(ViewModelComponent::class)
interface PictureModule {

    @Binds
    fun pictureRepository(repository: DefaultPictureRepository): PictureRepository

    @Binds
    fun bindLoad(impl: DefaultLoadPhoto): LoadPhotoUseCase

    @Binds
    fun bindSave(impl: DefaultSavePhoto): SavePhotoUseCase

    @Binds
    fun bindDelete(impl: DefaultDeletePhoto): DeletePhotoUseCase

}