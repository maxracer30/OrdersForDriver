package ru.maxstelmakh.ordersfordriver.data.yandexDiskS3Api

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.maxstelmakh.ordersfordriver.data.orderApi.Result
import ru.maxstelmakh.ordersfordriver.data.yandexDiskS3Api.model.repsonse.LinkToDownload
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

    override suspend fun uploadPhoto(pictureName: String): Result<LinkToDownload> = withContext(Dispatchers.IO) {
        Log.d("StatesOfApp", "1")

        return@withContext try {

            val photo = getPhoto(pictureName) ?: return@withContext Result.Failure(statusCode = 600)
            Log.d("StatesOfApp", "2 photo = " + photo.name)

            val linkResponse = apiPhoto.connectToServer(path = photo.name)


            Log.d("StatesOfApp", "3 linkResp ${linkResponse.isSuccessful}")

            val requestFile = photo.asRequestBody("image/jpg".toMediaTypeOrNull())
            Log.d("StatesOfApp", "4 reqFile ${requestFile.contentLength()} , ${requestFile.contentType()}")

            val file = MultipartBody.Part.createFormData("file", photo.name, requestFile)
            Log.d("StatesOfApp", "5 file ${file.headers} , ${file.headers?.size}")

            val upload = linkResponse.body()?.href?.let {
                apiPhoto.uploadFile(
                    url = it,
                    media = file
                )
            }
            Log.d("StatesOfApp", "6 ${upload?.isSuccessful}")

            Log.d("StatesOfApp", "7")

            val path = "$pictureName.jpg"
            Log.d("StatesOfApp", "8 path $path")

            val linkDownload = apiPhoto.getLinkToDownload(path = path)

            Log.d("StatesOfApp", "9 linkDown = $linkDownload ")

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