package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private fun setOrderDataToView() = with(binding) {

        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.order.collect {
                when (it.isEmpty()) {
                    true -> {}
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

                            setVisibilityOnView()
                        }
                    }
                }
            }

        }
    }


    private fun setVisibilityOnView() = with(binding) {

        progressBar.visibility = View.GONE

        openButton.visibility = View.VISIBLE
        completeOrderButton.visibility = View.VISIBLE

        completeOrderButton.setOnClickListener {
            Toast.makeText(context, "Ku-ku", Toast.LENGTH_SHORT).show()
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

    private fun hideRecyclerView() = with(binding) {
        openButton.text = res(R.string.open_goods)
        goodsRecyclerView.apply {
            visibility = View.VISIBLE
            translationY = 0f
            alpha = 1.0f
            animate()
                .translationY(-100f)
                .alpha(0.0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        goodsRecyclerView.visibility = View.GONE
                    }
                })
        }
    }


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
            translationY = -100f
            alpha = 0.0f
            animate()
                .translationY(0f)
                .alpha(1.0f)
                .setListener(null)
        }
        openButton.text = res(R.string.hide_goods)
    }

    override fun onClick(goods: Goods) {
        ChangeGoodsFragment(
            onConfirmClickListener = { count ->
                Toast.makeText(context, count, Toast.LENGTH_SHORT).show()
            },
            goods = goods
        ).show(parentFragmentManager, "Dialog")
    }

    private fun res(id: Int) = resources.getString(id)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}