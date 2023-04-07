package ru.maxstelmakh.ordersfordriver.data.yandexDiskApi

import android.content.Context
import android.graphics.Bitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.yandexDiskApi.model.repsonse.LinkToDownload
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRemoteRepository
import ru.maxstelmakh.ordersfordriver.domain.repositories.PictureRepository
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class DefaultApiPhoto @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiPhoto: APIPhoto,
    private val pictureRepository: PictureRepository
) : PictureRemoteRepository {

    override suspend fun uploadPhoto(photoName: String): Result<LinkToDownload> =
        withContext(Dispatchers.IO) {

            return@withContext try {

                val photo =
                    getPhoto(photoName) ?: return@withContext Result.Failure(statusCode = 600)

                val linkResponse = apiPhoto.connectToServer(path = photo.name)

                val requestFile = photo.asRequestBody("image/jpg".toMediaTypeOrNull())

                val file = MultipartBody.Part.createFormData("file", photo.name, requestFile)

                linkResponse.body()?.href?.let {
                    apiPhoto.uploadFile(
                        url = it,
                        media = file
                    )
                }

                val path = "$photoName.jpg"

                val linkDownload = apiPhoto.getLinkToDownload(path = path)

                return@withContext Result.Success(linkDownload.body()!!)

            } catch (e: Exception) {
                Result.Failure(statusCode = 0, message = e.message)
            }
        }

    private suspend fun getPhoto(name: String): File? = withContext(Dispatchers.IO) {
        try {
            val filesDir = context.filesDir
            val bitmap = pictureRepository.loadPhoto(name)
            val imageFile = File(filesDir, "$name.jpg")
            val os: OutputStream
            try {
                os = FileOutputStream(imageFile)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.flush()
                os.close()
            } catch (e: Exception) {
                return@withContext null
            }
            return@withContext imageFile
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}