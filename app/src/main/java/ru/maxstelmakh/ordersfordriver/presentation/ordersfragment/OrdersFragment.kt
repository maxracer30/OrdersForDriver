package ru.maxstelmakh.ordersfordriver.presentation.ordersfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.databinding.FragmentOrdersBinding
import ru.maxstelmakh.ordersfordriver.presentation.adapter.OrderAdapter

@AndroidEntryPoint
class OrdersFragment() : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OrdersViewModel>()
//    private val adapter = OrderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.ordersRecyclerView.adapter = adapter

        viewModel.viewModelScope.launch {
            viewModel.order.collect {
                with(binding) {
                    dateOrder.text = it.orderDate
                    numberOrder.text = it.orderNum
                    phoneOrder.text = it.phone
                    open.visibility = View.VISIBLE
                    open.setOnClickListener {
                        findNavController().navigate(R.id.action_ordersFragment_to_goodsFragment)
                    }
                }

//                adapter.submitList(listOf(it))
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}