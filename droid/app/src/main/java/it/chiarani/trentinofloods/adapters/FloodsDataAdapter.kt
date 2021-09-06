package it.chiarani.trentinofloods.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.chiarani.trentinofloods.databinding.ItemFloodDataBinding
class FloodsDataAdapter(private val data: List<String>)
    :RecyclerView.Adapter<FloodsDataAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemFloodDataBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        with(holder){
            with(data[position]) {
                val splitted = this.split(";")
                binding.date.text = splitted[0]
                binding.value.text = splitted[1]
            }
        }
    }

    inner class DataViewHolder(val binding: ItemFloodDataBinding)
        :RecyclerView.ViewHolder(binding.root)

}