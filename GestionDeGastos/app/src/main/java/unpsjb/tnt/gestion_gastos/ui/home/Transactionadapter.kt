package unpsjb.tnt.gestion_gastos.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import unpsjb.tnt.gestion_gastos.R
import unpsjb.tnt.gestion_gastos.data.model.CategoryType
import unpsjb.tnt.gestion_gastos.data.model.Transaction
import java.text.NumberFormat
import java.util.Locale

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon:     TextView = view.findViewById(R.id.tv_tx_icon)
        val tvDesc:     TextView = view.findViewById(R.id.tv_tx_description)
        val tvCategory: TextView = view.findViewById(R.id.tv_tx_category)
        val tvAmount:   TextView = view.findViewById(R.id.tv_tx_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tx  = getItem(position)
        val fmt = NumberFormat.getNumberInstance(Locale("es", "AR"))
            .apply { maximumFractionDigits = 2; minimumFractionDigits = 0 }

        holder.tvIcon.text     = tx.category.icon
        holder.tvDesc.text     = tx.description.ifBlank { tx.category.name }
        holder.tvCategory.text = tx.category.name

        // Gastos no llevan signo, ingresos llevan +
        val sign = if (tx.type == CategoryType.INGRESO) "+ " else ""
        holder.tvAmount.text = "$sign$ ${fmt.format(tx.amount)}"

        val colorRes = if (tx.type == CategoryType.INGRESO)
            R.color.income_green else R.color.expense_red
        holder.tvAmount.setTextColor(
            androidx.core.content.ContextCompat.getColor(holder.itemView.context, colorRes)
        )
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(a: Transaction, b: Transaction) = a.id == b.id
            override fun areContentsTheSame(a: Transaction, b: Transaction) = a == b
        }
    }
}