@file:Suppress("DEPRECATION")

package ru.maxstelmakh.ordersfordriver.presentation.goodsfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.os.BuildCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.maxstelmakh.ordersfordriver.R
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Goods
import ru.maxstelmakh.ordersfordriver.data.orderApi.model.Order
import ru.maxstelmakh.ordersfordriver.databinding.FragmentGoodsBinding
import ru.maxstelmakh.ordersfordriver.presentation.adapter.GoodsAdapter
import ru.maxstelmakh.ordersfordriver.presentation.adapter.GoodsClickListener
import ru.maxstelmakh.ordersfordriver.presentation.ordersfragment.OrdersViewModel

@BuildCompat.PrereleaseSdkCheck
@AndroidEntryPoint
class GoodsFragment : Fragment(), GoodsClickListener {
    private var _binding: FragmentGoodsBinding? = null
    private val binding get() = _binding!!

    private val adapter = GoodsAdapter(this)
    private val viewModel by viewModels<OrdersViewModel>()


    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoodsBinding.inflate(layoutInflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            goodsRecyclerView.adapter = adapter

            goodsRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )

            viewModel.viewModelScope.launch {
                viewModel.order.collect { order ->
                    order[0].goods?.let { goodsList ->
                        adapter.submitList(goodsList)
                    }
                }
            }

        }
//        setOnBackPressed()
    }

//    private fun setOnBackPressed() {
//        val bundle = Bundle()
//        bundle.putSerializable("orderToChange", viewModel.order)
//
//        if (BuildCompat.isAtLeastT()) {
//            requireActivity().onBackInvokedDispatcher.registerOnBackInvokedCallback(
//                OnBackInvokedDispatcher.PRIORITY_DEFAULT
//            ) {
//                findNavController().navigate(
//                    R.id.action_goodsFragment_to_ordersFragment,
//                    bundle
//                )
//            }
//        } else {
//            requireActivity().onBackPressedDispatcher.addCallback(
//                this.viewLifecycleOwner,
//                object : OnBackPressedCallback(true) {
//                    override fun handleOnBackPressed() {
//                        findNavController().navigate(
//                            R.id.action_goodsFragment_to_ordersFragment,
//                            bundle
//                        )
//                    }
//                })
//        }
//    }

    override fun onClick(goods: Goods) {
        val bundle = bundleOf("goodsToChange" to goods)
        findNavController().navigate(
            R.id.action_goodsFragment_to_changeGoodsFragment,
            bundle
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
