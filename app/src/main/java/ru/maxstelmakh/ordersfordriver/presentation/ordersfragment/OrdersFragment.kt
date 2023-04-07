package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.databinding.FragmentOrdersBinding
import ru.maxstelmakh.ordersfordriver.domain.model.GoodsToChange
import ru.maxstelmakh.ordersfordriver.presentation.adapter.GoodsAdapter
import ru.maxstelmakh.ordersfordriver.presentation.adapter.GoodsClickListener
import ru.maxstelmakh.ordersfordriver.presentation.changegoodsfragment.ChangeGoodsFragment


@Suppress("DEPRECATION")
@AndroidEntryPoint
class OrdersFragment : Fragment(), GoodsClickListener {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private val goodsAdapter = GoodsAdapter(this)

    private val viewModel by viewModels<OrdersViewModel>()


    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOrderDataToView()
    }

    // Устанавливает данные заказа во вьюшку
    private fun setOrderDataToView() = with(binding) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.order.collect {
                when (it.isEmpty()) {
                    true -> {
                        constraintLayout.visibility = View.GONE
                    }
                    else -> {
                        withContext(Dispatchers.Main) {
                            dateOrder.text =
                                buildString { append(res(R.string.date), it[0].orderDate) }
                            numberOrder.text =
                                buildString { append(res(R.string.orderer_number), it[0].orderNum) }
                            phoneOrder.text =
                                buildString { append(res(R.string.order_number), it[0].phone) }
                            goodsCount.text =
                                buildString {
                                    append(
                                        res(R.string.goods_count),
                                        it[0].goods?.size.toString()
                                    )
                                }

                            it[0].goods?.let { goods -> goodsAdapter.submitList(goods) }

                            if (!goodsRecyclerView.isVisible) {
                                setVisibilityOnView()
                            }
                        }
                    }
                }
            }
        }
    }


    // Изменение видимости некоторых элементов
    private fun setVisibilityOnView() = with(binding) {

        showConstraintLayout()

        showOpenButton()

        showCompleteOrderButton()

        hideProgressBar()

        completeOrderButton.setOnClickListener {

            viewModel.completeOrder()

            showProgressBar()

            if (goodsRecyclerView.isVisible) {
                hideRecyclerView()
            }
            hideConstraintLayout()

            hideOpenButton()

            hideCompleteOrderButton()

        }

        var isRecyclerVisible = false
        openButton.setOnClickListener {
            when (isRecyclerVisible) {
                true -> hideRecyclerView()
                else -> showRecyclerView()
            }
            isRecyclerVisible = !isRecyclerVisible
        }
    }

    private fun showProgressBar() {
        binding.progressBar.apply {
            visibility = View.VISIBLE
            translationY = -300f
            alpha = 0.0f
            animate()
                .translationY(0f)
                .alpha(1.0f)
                .setListener(null)
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.apply {
            visibility = View.VISIBLE
            translationY = 0f
            alpha = 1.0f
            animate()
                .translationY(300f)
                .alpha(0.0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        binding.progressBar.visibility = View.GONE
                    }
                })
        }
    }

    // Управляет сокрытием RecyclerView
    private fun hideRecyclerView() = with(binding) {
        openButton.text = res(R.string.open_goods)
        goodsRecyclerView.apply {
            visibility = View.VISIBLE
            translationY = 0f
            alpha = 1.0f
            animate()
                .translationY(-200f)
                .alpha(0.0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        goodsRecyclerView.visibility = View.GONE
                    }
                })
        }
    }

    // Управляет отображением RecyclerView
    private fun showRecyclerView() = with(binding) {
        goodsRecyclerView.adapter = goodsAdapter
        goodsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        goodsRecyclerView.apply {
            visibility = View.VISIBLE
            translationY = -200f
            alpha = 0.0f
            animate()
                .translationY(0f)
                .alpha(1.0f)
                .setListener(null)
        }
        openButton.text = res(R.string.hide_goods)
    }

    // Управляет сокрытием заказа
    private fun hideConstraintLayout() {
        binding.constraintLayout.apply {
            visibility = View.GONE
            translationY = 0f
            alpha = 1.0f
            animate()
                .translationY(-200f)
                .alpha(0.0f)
                .setListener(null)
        }
    }

    // Управляет отображением заказа
    private fun showConstraintLayout() {
        binding.constraintLayout.apply {
            visibility = View.VISIBLE
            translationY = -200f
            alpha = 0.0f
            animate()
                .translationY(0f)
                .alpha(1.0f)
                .setListener(null)
        }
    }

    // Управляет отображением кнопки завершения заказа
    private fun showCompleteOrderButton() {
        binding.completeOrderButton.apply {
            visibility = View.VISIBLE
            isClickable = true
            translationY = 200f
            alpha = 0.0f
            animate()
                .translationY(0f)
                .alpha(1.0f)
                .setListener(null)
        }
    }

    // Управляет сокрытием кнопки завершения заказа
    private fun hideCompleteOrderButton() {
        binding.completeOrderButton.apply {
            isClickable = false
            translationY = 0f
            alpha = 1.0f
            animate()
                .translationY(200f)
                .alpha(0.0f)
                .setListener(null)
        }
    }

    // Управляет отображением кнопки показа товаров
    private fun showOpenButton() {
        binding.openButton.apply {
            visibility = View.VISIBLE
            isClickable = true
            translationY = 200f
            alpha = 0.0f
            animate()
                .translationY(0f)
                .alpha(1.0f)
                .setListener(null)
        }
    }

    // Управляет сокрытием кнопки показа товаров
    private fun hideOpenButton() {
        binding.openButton.apply {
            isClickable = false
            translationY = 0f
            alpha = 1.0f
            animate()
                .translationY(200f)
                .alpha(0.0f)
                .setListener(null)
        }
    }

    // Слушатель нажатий на товар в RecyclerView
    override fun onClick(goods: Goods) {
        viewModel.changedGoods = GoodsToChange(
            item = goods,
            viewModel.changeGoodsReasons[goods.article] ?: ""
        )

        ChangeGoodsFragment(
            originalGoods = viewModel.getOriginalGoods(),
            goodsToChange = viewModel.changedGoods,
            changedGoodsListener = {
                viewModel.changedGoods.item = it
                viewModel.saveGoods()
            }
        ).show(parentFragmentManager, "Dialog")
    }

    // Для доступа к строковым ресурсам
    private fun res(id: Int) = resources.getString(id)

    // Зануляем binding
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}