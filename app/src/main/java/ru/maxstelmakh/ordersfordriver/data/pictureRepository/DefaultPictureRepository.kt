package ru.maxstelmakh.ordersfordriver.data.pictureRepository

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.system.ErrnoException
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.maxstelmakh.ordersfordriver.data.pictureRepository.implusecases.DefaultLoadPhoto
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository
import java.io.IOException
import javax.inject.Inject

class DefaultPictureRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : PictureRepository {

    override suspend fun savePhoto(name: String, bitmap: Bitmap): Boolean {
        return try {
            Contexts.getApplication(context)
                .openFileOutput("$name.jpg", Activity.MODE_PRIVATE).use { stream ->
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)) {
                        throw IOException("Could not save bitmap")
                    }

                }
            true

        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun loadPhoto(name: String): Bitmap? {
        var savedBitmap: Bitmap? = null
        try {
            CoroutineScope(Dispatchers.IO).launch {
                try{
                    savedBitmap = BitmapFactory.decodeStream(
                        Contexts.getApplication(context)
                            .openFileInput("$name.jpg")
                            .readBytes()
                            .inputStream()
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: ErrnoException) {
            e.printStackTrace()
        }
        return savedBitmap
    }
}