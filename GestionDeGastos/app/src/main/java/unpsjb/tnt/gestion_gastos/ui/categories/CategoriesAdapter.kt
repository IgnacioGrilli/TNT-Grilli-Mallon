package unpsjb.tnt.gestion_gastos.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import unpsjb.tnt.gestion_gastos.R
import unpsjb.tnt.gestion_gastos.data.model.Category

class CategoriesAdapter : ListAdapter<Category, CategoriesAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon:     TextView = view.findViewById(R.id.tv_tx_icon)
        val tvDesc:     TextView = view.findViewById(R.id.tv_tx_description)
        val tvCategory: TextView = view.findViewById(R.id.tv_tx_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category  = getItem(position)

        holder.tvIcon.text     = category.icon
        holder.tvDesc.text     = category.name
        holder.tvCategory.text = ""
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(a: Category, b: Category) = a.id == b.id
            override fun areContentsTheSame(a: Category, b: Category) = a == b
        }
    }
}