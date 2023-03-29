@file:Suppress("DEPRECATION")

package ru.maxstelmakh.ordersfordriver.presentation.goodsfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.maxstelmakh.ordersfordriver.data.model.Order
import ru.maxstelmakh.ordersfordriver.databinding.FragmentGoodsBinding
import ru.maxstelmakh.ordersfordriver.presentation.adapter.GoodsAdapter

@AndroidEntryPoint
class GoodsFragment : Fragment() {
    private var _binding: FragmentGoodsBinding? = null
    private val binding get() = _binding!!

    private val adapter = GoodsAdapter()
    private val viewModel by viewModels<GoodsViewModel>()

    var order: Order? = null

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        order = arguments?.getSerializable("orderToChange") as Order

        _binding = FragmentGoodsBinding.inflate(layoutInflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            goodsRecyclerView.adapter = adapter

            order?.goods?.let {
                adapter.submitList(it)
            }

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}