package com.ht117.skeleton.demo

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ht117.skeleton.ISkeleton
import com.ht117.skeleton.demo.databinding.FragmentNormalBinding
import com.ht117.skeleton.demo.databinding.FragmentRvBinding
import com.ht117.skeleton.generateSkeleton

class PagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {

    private val items = listOf(FragmentNormal(), FragmentRv())

    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }

    fun getItem(pos: Int): BaseFragment {
        require(pos < items.size) { "This value should be less than list size" }
        return items[pos]
    }
}

abstract class BaseFragment(@LayoutRes layoutId: Int): Fragment(layoutId) {
    abstract fun setupView()
    abstract fun showSkeleton()
    abstract fun showOriginal()
}

class FragmentNormal: BaseFragment(R.layout.fragment_normal) {

    private lateinit var binding: FragmentNormalBinding
    private var skeletons = mutableListOf<ISkeleton>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNormalBinding.bind(view)
        setupView()
    }

    override fun setupView() {
        binding.run {
            skeletons = root.children.map { it.generateSkeleton() }.toMutableList()
        }
    }

    override fun showSkeleton() {
        skeletons.forEach { it.showSkeleton() }
    }

    override fun showOriginal() {
        skeletons.forEach { it.showOriginal() }
    }
}

class FragmentRv: BaseFragment(R.layout.fragment_rv) {

    private lateinit var binding: FragmentRvBinding
    private var skeletons = mutableListOf<ISkeleton>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRvBinding.bind(view)
        setupView()
    }

    override fun setupView() {
        binding.run {
            root.adapter = DemoAdapter(DemoAdapter.DATA)

            skeletons = mutableListOf(root.generateSkeleton(layoutId = R.layout.item_view_demo, itemCount = 5))
        }
    }

    override fun showSkeleton() {
        skeletons.forEach { it.showSkeleton() }
    }

    override fun showOriginal() {
        skeletons.forEach { it.showOriginal() }
    }
}

