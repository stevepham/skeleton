package com.ht117.skeleton.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.ht117.skeleton.SkeletonLayout
import com.ht117.skeleton.generateSkeleton

internal class SkeletonAdapter(
    @LayoutRes private val layoutId: Int,
    private val itemCount: Int
) : RecyclerView.Adapter<SkeletonAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val orgView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val skeleton = orgView.generateSkeleton() as SkeletonLayout

        skeleton.layoutParams = orgView.layoutParams
        skeleton.showSkeleton()

        return Holder(skeleton)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) = Unit

    override fun getItemCount() = itemCount

    internal class Holder(itemView: SkeletonLayout) : RecyclerView.ViewHolder(itemView)
}