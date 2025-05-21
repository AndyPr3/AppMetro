package com.upc.appmetropolitano.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.models.TransactionModel
import java.text.NumberFormat
import java.util.*

class MovimientosAdapter(
    private var items: List<TransactionModel>,
    private val onItemClick: (TransactionModel) -> Unit
) : RecyclerView.Adapter<MovimientosAdapter.MovimientoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movimiento, parent, false)
        return MovimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val mov = items[position]
        holder.bind(mov)
        holder.itemView.setOnClickListener { onItemClick(mov) }
    }

    fun updateData(newItems: List<TransactionModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    class MovimientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate     = itemView.findViewById<TextView>(R.id.txtFecha)
        private val tvAmount   = itemView.findViewById<TextView>(R.id.txtSaldo)

        fun bind(m: TransactionModel) {

            tvDate.text = m.transactionDate

            val nf = NumberFormat.getCurrencyInstance(Locale("es","PE"))
            val formatted = nf.format(m.amount)
            tvAmount.text = if (m.type.uppercase() == "V") "- $formatted" else formatted

            if (m.type.uppercase() == "V") {
                tvAmount.setTextColor(itemView.context.getColor(R.color.red))
            } else {
                tvAmount.setTextColor(itemView.context.getColor(R.color.black))
            }
        }
    }
}
