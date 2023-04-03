package ru.maxstelmakh.ordersfordriver.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.maxstelmakh.ordersfordriver.data.pictureRepository.DefaultPictureRepository
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository

@Module
@InstallIn(ViewModelComponent::class)
interface PictureModule {

    @Binds
    fun pictureRepository(repository: DefaultPictureRepository): PictureRepository


}