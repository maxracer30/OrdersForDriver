package ru.maxstelmakh.ordersfordriver.presentation.changegoodsfragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.databinding.ChangeDialogBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class ChangeGoodsFragment(
    private val originalGoods: Goods,
    private val goodsToChange: Goods,
    private val changedGoodsListener: (Goods) -> Unit
) : DialogFragment() {

    companion object {
        private const val REQUEST_TAKE_PHOTO = 0
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
        private const val CAMERA_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
    }

    private var _binding: ChangeDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ChangeViewModel>()

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = ChangeDialogBinding.inflate(LayoutInflater.from(context))

        viewModel.originalGoods = originalGoods
        viewModel.changedGoods = goodsToChange

        val builder = AlertDialog.Builder(requireActivity())

        builder.setView(binding.root)

        binding.cardPhoto1.setOnClickListener {
            takePhoto()
        }

        binding.cardPhoto2.setOnClickListener {
            selectImageInAlbum()
        }

        setGoodsToView()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.TOP)

        return dialog
    }

    //Устанавливает значения на вьюшку
    private fun setGoodsToView() = with(binding) {


        val goods = viewModel.changedGoods


        tvName.text = goods.name
        tvPrice.text = buildString { append(goods.price.toString(), res(R.string.perPiece)) }
        tvSumm.text = buildString { append(res(R.string.summary), goods.summ.toString()) }
        etCount.setText(goods.quantity.toString(), TextView.BufferType.EDITABLE)

        visibilityChangingFields()

        increaseCount.setOnClickListener { increaseInteger() }
        decreaseCount.setOnClickListener { decreaseInteger() }

        etCount.doAfterTextChanged {
            when (checkCountIsInt()) {
                true -> {
                    viewModel.newCount = etCount.text.toString().toInt()
                    viewModel.setChangedGoods()
                    visibilityChangingFields()
                    changeSum()
                    decreaseBtnActive()
                }
                false -> {}
            }
        }


        confirmBtn.setOnClickListener {
            when (checkCount()) {
                true -> {
                    changedGoodsListener(viewModel.originalGoods)
                    dismiss()
                }
                false -> {
                    if (etReason.isVisible && etReason.text.toString().isNotBlank()) {
                        changedGoodsListener(viewModel.changedGoods)
                        dismiss()
                    } else {
                        tvAttention.text = res(R.string.attention)
                        tvAttention.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    //Изменяет видимость фотокарточек и поля ввода причины изменений
    private fun visibilityChangingFields() = with(binding) {
        when (checkCount()) {
            true -> {
                cardPhoto1.visibility = View.GONE
                cardPhoto2.visibility = View.GONE
                cardPhoto3.visibility = View.GONE
                tilReason.visibility = View.GONE
                tvAttention.visibility = View.GONE
            }
            false -> {
                cardPhoto1.visibility = View.VISIBLE
                cardPhoto2.visibility = View.VISIBLE
                cardPhoto3.visibility = View.VISIBLE
                tilReason.visibility = View.VISIBLE
            }
        }

    }


    // Инкремент количества товара
    private fun increaseInteger() = with(binding) {

        when (etCount.text.toString().toIntOrNull()) {
            is Int -> {
                val newCount = etCount.text.toString().toInt() + 1
                etCount.setText(newCount.toString(), TextView.BufferType.EDITABLE)
            }
            else -> {
                etCount.setText(goodsToChange.quantity.toString(), TextView.BufferType.EDITABLE)
            }
        }
    }

    // Декремент количества товара
    private fun decreaseInteger() = with(binding) {
        when (etCount.text.toString().toIntOrNull()) {
            is Int -> {
                val newCount = etCount.text.toString().toInt() - 1
                etCount.setText(newCount.toString(), TextView.BufferType.EDITABLE)
            }
            else -> {
                etCount.setText(goodsToChange.quantity.toString(), TextView.BufferType.EDITABLE)
            }
        }
    }

    // Изменение активности кнопки декремента
    private fun decreaseBtnActive() = with(binding) {
        decreaseCount.isClickable =
            when (etCount.text.toString().toInt()) {
                0 -> false
                else -> true
            }
    }

    //Изменение итоговой суммы
    private fun changeSum() = with(binding) {
        tvSumm.text =
            when (checkCount()) {
                true -> buildString { append(res(R.string.summary), originalGoods.summ.toString()) }
                false -> buildString {
                    append(
                        res(R.string.estim_amount),
                        viewModel.changedGoods.summ.toString()
                    )
                }
            }
    }

    // Проверка вводимого числа на соответствие типу Int
    private fun checkCountIsInt(): Boolean {
        return when (binding.etCount.text.toString().toIntOrNull()) {
            is Int -> true
            else -> false
        }
    }

    //Проверка количества товаров на равность к исходному зачению
    private fun checkCount(): Boolean {
        return when (checkCountIsInt()) {
            true -> originalGoods.quantity == binding.etCount.text.toString().toInt()
            else -> false
        }
    }


    private fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }

    private fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
        if (intent1.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        contentURI
                    )
                    val path = saveImage(bitmap)
                    Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
                    binding.photo2.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            binding.photo1.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString()
        )
        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                requireContext(),
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    // Доступ к строковым ресурсам
    private fun res(id: Int) = resources.getString(id)

    // Зануляем binding
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
