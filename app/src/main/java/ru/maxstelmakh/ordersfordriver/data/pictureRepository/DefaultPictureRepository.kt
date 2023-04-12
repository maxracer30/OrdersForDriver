package ru.maxstelmakh.ordersfordriver.data.pictureRepository

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository
import java.io.File
import java.io.IOException
import javax.inject.Inject

class DefaultPictureRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : PictureRepository {

    override suspend fun savePhoto(name: String, bitmap: Bitmap): Boolean {
        return try {
            Contexts.getApplication(context)
                .openFileOutput("$name.jpg", Activity.MODE_PRIVATE).use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    stream.close()
                }
            true

        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun loadPhoto(name: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            return@withContext BitmapFactory.decodeStream(
                Contexts.getApplication(context)
                    .openFileInput("$name.jpg")
                    .readBytes()
                    .inputStream()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }

    override suspend fun deletePhoto(name: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val fileDir = context.filesDir
            val file = File(fileDir, "$name.jpg")
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}