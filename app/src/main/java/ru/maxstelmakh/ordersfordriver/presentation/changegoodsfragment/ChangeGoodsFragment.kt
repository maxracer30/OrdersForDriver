@file:Suppress("DEPRECATION")
@file:SuppressLint("NewApi")

package ru.maxstelmakh.ordersfordriver.presentation.changegoodsfragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.databinding.ChangeDialogBinding
import ru.maxstelmakh.ordersfordriver.domain.model.GoodsToChange
import java.io.*
import java.util.*


@AndroidEntryPoint
class ChangeGoodsFragment(
    private val originalGoods: Goods,
    private val goodsToChange: GoodsToChange,
    private val changedGoodsListener: (Goods) -> Unit
) : DialogFragment() {

    companion object {
        private const val TAKE_PHOTO_REQUEST_CODE = 10
        private const val SELECT_IMAGE_IN_ALBUM_REQUEST_CODE = 11
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

        setGoodsToView()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.TOP)

        return dialog
    }

    //Устанавливает значения в вьюшку
    private fun setGoodsToView() = with(binding) {


        val goods = viewModel.changedGoods.item


        tvName.text = goods.name
        tvPrice.text = buildString { append(goods.price.toString(), res(R.string.perPiece)) }
        tvSumm.text = buildString { append(res(R.string.summary), goods.summ.toString()) }
        etCount.setText(goods.quantity.toString(), TextView.BufferType.EDITABLE)
        etReason.setText(viewModel.changedGoods.changeReason, TextView.BufferType.EDITABLE)


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

        etReason.doAfterTextChanged {
            when (etReason.text.isNullOrBlank()) {
                true -> {}
                false -> {
                    viewModel.changedGoods.changeReason = etReason.text.toString()
                }
            }
        }


        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.photo.collect { bitmap ->
                withContext(Dispatchers.Main) {
                    binding.photo1.setImageBitmap(bitmap)
                }
            }
        }

        cameraBtn.setOnClickListener {
            when (requestAllPermissions()) {
                true -> takePhoto()
                else -> return@setOnClickListener
            }
        }

        galleryBtn.setOnClickListener {
            when (requestAllPermissions()) {
                true -> selectImageInAlbum()
                else -> return@setOnClickListener
            }
        }

        confirmBtn.setOnClickListener {
            when (checkCount()) {
                true -> {
                    changedGoodsListener(viewModel.originalGoods)
                    dismiss()
                }
                false -> {
                    if (etReason.text.toString().isNotBlank() && viewModel.checkHavePhoto) {
                        changedGoodsListener(viewModel.changedGoods.item)
                        dismiss()
                    } else {
                        tvAttention.text = res(R.string.attention)
                        tvAttention.visibility = View.VISIBLE
                        cardPhoto1.cardElevation = 70f
                        cardPhoto1.outlineSpotShadowColor = Color.RED
                        cardPhoto1.alpha = 1f
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
                cameraBtn.visibility = View.GONE
                galleryBtn.visibility = View.GONE
                tilReason.visibility = View.GONE
                tvAttention.visibility = View.GONE
                cardPhoto1.cardElevation = 0f
            }
            false -> {
                cardPhoto1.visibility = View.VISIBLE
                cameraBtn.visibility = View.VISIBLE
                galleryBtn.visibility = View.VISIBLE
                tilReason.visibility = View.VISIBLE
                cardPhoto1.outlineSpotShadowColor = Color.GRAY
                viewModel.loadPhoto(goodsToChange.item.article.toString())
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
                etCount.setText(
                    goodsToChange.item.quantity.toString(),
                    TextView.BufferType.EDITABLE
                )
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
                etCount.setText(
                    goodsToChange.item.quantity.toString(),
                    TextView.BufferType.EDITABLE
                )
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
                        viewModel.changedGoods.item.summ.toString()
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


    // Выбор фото из галереи
    @SuppressLint("QueryPermissionsNeeded")
    private fun selectImageInAlbum() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, SELECT_IMAGE_IN_ALBUM_REQUEST_CODE)
        }
    }

    // Сделать фото
    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhoto() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
        }
    }


    // Обрабатывает результат фото
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SELECT_IMAGE_IN_ALBUM_REQUEST_CODE -> {
                data?.let {
                    val contentURI = it.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            contentURI
                        )

                        viewModel.savePhoto(
                            name = originalGoods.article.toString(),
                            bitmap = bitmap
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }


            TAKE_PHOTO_REQUEST_CODE -> {

                data?.let {
                    val thumbnail = it.extras?.get("data") as Bitmap
                    viewModel.savePhoto(
                        name = originalGoods.article.toString(),
                        bitmap = thumbnail
                    )
                }
            }
            else -> return
        }
    }

    private fun requestAllPermissions(): Boolean {
        var info: PackageInfo? = null
        try {
            info = requireActivity().packageManager.getPackageInfo(
                requireContext().packageName,
                PackageManager.GET_PERMISSIONS
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (info == null) {
            return true
        }
        val permissions = info.requestedPermissions
        var remained = false
        for (permission in permissions) {
            if (checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                remained = true
            }
        }
        return if (remained) {
            requestPermissions(permissions, 0)
            false
        } else true
    }

    // Доступ к строковым ресурсам
    private fun res(id: Int) = resources.getString(id)

    // Зануляем binding
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
