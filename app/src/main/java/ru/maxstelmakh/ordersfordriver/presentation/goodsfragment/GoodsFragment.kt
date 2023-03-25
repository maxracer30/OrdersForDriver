package ru.maxstelmakh.ordersfordriver.presentation.goodsfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.maxstelmakh.ordersfordriver.databinding.FragmentGoodsBinding

@AndroidEntryPoint
class GoodsFragment : Fragment() {
    private var _binding: FragmentGoodsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoodsBinding.inflate(layoutInflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}