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
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.databinding.ChangeDialogBinding
import ru.maxstelmakh.ordersfordriver.presentation.ordersfragment.OrdersViewModel
import kotlin.math.roundToInt

@AndroidEntryPoint
class ChangeGoodsFragment() : DialogFragment() {

    private lateinit var binding: ChangeDialogBinding

    private val viewModel by viewModels<OrdersViewModel>()

    val goods = viewModel.goodsToChange

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = ChangeDialogBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())

        with(binding) {
            builder.setView(root)

            tvName.text = goods.name
            tvPrice.text = buildString { append(goods.price.toString(), res(R.string.perPiece)) }
            tvSumm.text = buildString { append(res(R.string.summary), goods.summ.toString()) }
            etCount.setText(goods.quantity.toString(), TextView.BufferType.EDITABLE)

            visibilityChangingFields()

            increaseCount.setOnClickListener { increaseInteger() }
            decreaseCount.setOnClickListener { decreaseInteger() }

            etCount.doAfterTextChanged {
                when (etCount.text.toString().toIntOrNull()) {
                    is Int -> {
                        visibilityChangingFields()
                        changeSum()
                        decreaseBtnActive()
                    }
                    else -> {}
                }
            }

            confirmBtn.setOnClickListener {
                viewModel.saveGoods()
                dismiss()
            }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.TOP)

        return dialog
    }

    private fun visibilityChangingFields() = with(binding) {
        when (checkCount()) {
            true -> {
                cardPhoto1.visibility = View.GONE
                cardPhoto2.visibility = View.GONE
                cardPhoto3.visibility = View.GONE
                tilReason.visibility = View.GONE
            }
            false -> {
                cardPhoto1.visibility = View.VISIBLE
                cardPhoto2.visibility = View.VISIBLE
                cardPhoto3.visibility = View.VISIBLE
                tilReason.visibility = View.VISIBLE

            }
        }

    }

    private fun increaseInteger() = with(binding) {

        when (etCount.text.toString().toIntOrNull()) {
            is Int -> {
                val newCount = etCount.text.toString().toInt() + 1
                etCount.setText(newCount.toString(), TextView.BufferType.EDITABLE)
            }
            else -> {
                etCount.setText(goods.quantity.toString(), TextView.BufferType.EDITABLE)
            }
        }
    }

    private fun decreaseInteger() = with(binding) {
        when (etCount.text.toString().toIntOrNull()) {
            is Int -> {
                val newCount = etCount.text.toString().toInt() - 1
                etCount.setText(newCount.toString(), TextView.BufferType.EDITABLE)
            }
            else -> {
                etCount.setText(goods.quantity.toString(), TextView.BufferType.EDITABLE)
            }
        }
    }

    private fun decreaseBtnActive() = with(binding) {
        decreaseCount.isClickable =
            when (etCount.text.toString().toInt()) {
                0 -> false
                else -> true
            }
    }

    private fun changeSum() = with(binding) {

        val sum = (goods.price * etCount.text.toString().toDouble() * 100.0).roundToInt() / 100.00

        tvSumm.text =
            when (checkCount()) {
                true -> buildString { append(res(R.string.summary), goods.summ.toString()) }
                false -> buildString { append(res(R.string.estim_amount), sum.toString()) }
            }
    }

    private fun checkCount() = goods.quantity == binding.etCount.text.toString().toInt()


    private fun res(id: Int) = resources.getString(id)
}
