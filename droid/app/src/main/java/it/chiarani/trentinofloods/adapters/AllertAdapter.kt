package it.chiarani.trentinofloods.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import it.chiarani.trentinofloods.data.AllertItem
import it.chiarani.trentinofloods.databinding.ItemAllertBinding
import it.chiarani.trentinofloods.databinding.ItemFloodDataBinding
import java.lang.String


class AllertAdapter(private val data: List<AllertItem>, private val onItemClickListener: OnItemClickListener)
    :RecyclerView.Adapter<AllertAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemAllertBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        holder.binding.root.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }

        with(holder){
            with(data[position]) {
                binding.itemAllertTxtDate.text = String.format(
                    "%s %s %s",
                    this.day,
                    this.month,
                    this.year
                )

                binding.itemAllertTxtName.setText(this.name)
                val dateDrawable: TextDrawable = TextDrawable
                    .builder()
                    .beginConfig()
                    .fontSize(28)
                    .endConfig()
                    .buildRoundRect(
                      this.month.toUpperCase(),
                        Color.parseColor("#350F4E"),
                        100
                    )
                dateDrawable.setPadding(2, 2, 2, 2)
                binding.itemAllertIcDate.setImageDrawable(dateDrawable)
            }
        }
    }

    inner class DataViewHolder(val binding: ItemAllertBinding)
        :RecyclerView.ViewHolder(binding.root)

}
