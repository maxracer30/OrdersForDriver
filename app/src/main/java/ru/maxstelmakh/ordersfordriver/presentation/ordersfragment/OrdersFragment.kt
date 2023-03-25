package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.databinding.FragmentOrdersBinding
import ru.maxstelmakh.ordersfordriver.presentation.adapter.OrderAdapter

@AndroidEntryPoint
class OrdersFragment() : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrdersViewModel by viewModels()
    private val adapter = OrderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ordersRecyclerView.adapter = adapter

        viewModel.viewModelScope.launch {
            viewModel.orders.observe(viewLifecycleOwner) { newOrders ->

                val orders = if (newOrders.isNotEmpty()) newOrders as ArrayList<Order>

                else ArrayList()
                println(orders)
                println(newOrders)
                adapter.submitList(orders)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}