package ru.maxstelmakh.ordersfordriver.presentation.changegoodsfragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.databinding.ChangeDialogBinding
import kotlin.math.roundToInt

@AndroidEntryPoint
class ChangeGoodsFragment(
    private val originalGoods: Goods,
    private val goodsToChange: Goods,
    private val changedGoodsListener: (Goods) -> Unit
) : DialogFragment() {

    private lateinit var binding: ChangeDialogBinding

    private val viewModel by viewModels<ChangeViewModel>()

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = ChangeDialogBinding.inflate(LayoutInflater.from(context))

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
                    if(etReason.isVisible && etReason.text.toString().isNotBlank()) {
                        changedGoodsListener(viewModel.changedGoods)
                        dismiss()
                    }
                    else {
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
                false -> buildString { append(res(R.string.estim_amount), viewModel.changedGoods.summ.toString()) }
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
        return when(checkCountIsInt()) {
            true -> originalGoods.quantity == binding.etCount.text.toString().toInt()
            else -> false
        }
    }


    // Доступ к ресурсам
    private fun res(id: Int) = resources.getString(id)
}
