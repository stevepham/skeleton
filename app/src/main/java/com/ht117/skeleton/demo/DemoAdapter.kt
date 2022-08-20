package com.ht117.skeleton.demo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ht117.skeleton.demo.databinding.ItemViewDemoBinding

data class DemoData(val img: Int, val title: String, val desc: String)

class DemoAdapter(private val items: List<DemoData>): RecyclerView.Adapter<DemoAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemViewDemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    inner class Holder(private val binding: ItemViewDemoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DemoData) {
            binding.run {
                iv.setImageResource(item.img)
                tvTitle.text = item.title
                tvDesc.text = item.desc
            }
        }
    }

    companion object {
        val DATA = listOf(
            DemoData(R.drawable.ic_account, "Title 1", "Description 1"),
            DemoData(R.drawable.ic_account, "Title 2", "Description 2"),
            DemoData(R.drawable.ic_account, "Title 3", "Description 3")
        )
    }
}