package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.databinding.FragmentOrdersBinding

@AndroidEntryPoint
class OrdersFragment() : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OrdersViewModel>()

    private var currentOrder: Order? = null

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentOrder = arguments?.getSerializable("orderToChange", Order::class.java)

        _binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println(currentOrder)
        when (currentOrder) {
            null -> {
                viewModel.viewModelScope.launch {
                    viewModel.getNewOrder()
                    viewModel.order.collect {
                        currentOrder = it
                        setOrderDataToView()
                    }
                }
            }
            else -> setOrderDataToView()
        }
    }

    private fun setOrderDataToView() {
        with(binding) {
            currentOrder?.let { order ->


                dateOrder.text = order.orderDate
                numberOrder.text = order.orderNum
                phoneOrder.text = order.phone
                goodsCount.text = order.goods?.size.toString()
                progressBar.visibility = View.GONE
                open.visibility = View.VISIBLE
                completeOrder.visibility = View.VISIBLE

                completeOrder.setOnClickListener {
                    Toast.makeText(context, "Ku-ku", Toast.LENGTH_SHORT).show()
                }

                open.setOnClickListener {

                    val bundle = Bundle()
                    bundle.putSerializable("orderToChange", order)

                    findNavController().navigate(
                        R.id.action_ordersFragment_to_goodsFragment,
                        bundle
                    )
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}