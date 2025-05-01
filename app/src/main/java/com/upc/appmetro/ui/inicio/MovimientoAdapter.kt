package com.upc.appmetro.ui.inicio

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.upc.appmetro.R
import com.upc.appmetro.data.model.Movimiento

class MovimientoAdapter(private val movimientos: List<Movimiento>) :
    RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder>() {

    class MovimientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFecha: TextView = itemView.findViewById(R.id.txtMovimientoFecha)
        val txtMonto: TextView = itemView.findViewById(R.id.txtMovimientoMonto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movimiento, parent, false)
        return MovimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val movimiento = movimientos[position]
        holder.txtFecha.text = movimiento.fecha

        // Formatea el monto y ajusta color si es negativo
        val esNegativo = movimiento.monto < 0
        holder.txtMonto.text = if (esNegativo)
            "- S/ %.2f".format(kotlin.math.abs(movimiento.monto))
        else
            "S/ %.2f".format(movimiento.monto)

        holder.txtMonto.setTextColor(
            if (esNegativo) Color.RED else Color.parseColor("#1A2D69")
        )
    }

    override fun getItemCount(): Int = movimientos.size
}
