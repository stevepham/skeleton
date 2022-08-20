package com.ht117.skeleton.rv

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.ht117.skeleton.ISkeleton

class SkeletonRecyclerView(private val rv: RecyclerView,
                           @LayoutRes private val layoutId: Int,
                           itemCount: Int): ISkeleton {

    private val originalAdapter = rv.adapter
    private var skeletonAdapter = SkeletonAdapter(layoutId, itemCount)

    override fun showSkeleton() {
        rv.adapter = skeletonAdapter
    }

    override fun showOriginal() {
        rv.adapter = originalAdapter
    }

    override fun isSkeleton() = rv.adapter == skeletonAdapter

}