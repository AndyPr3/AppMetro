package com.upc.appmetropolitano.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.models.BusRouteModel

class RutasAdapter(
    private val items: List<BusRouteModel>,
    private val onToggle: (position: Int) -> Unit
) : RecyclerView.Adapter<RutasAdapter.RutaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ruta, parent, false)
        return RutaViewHolder(view, onToggle)
    }

    override fun onBindViewHolder(holder: RutaViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class RutaViewHolder(
        itemView: View,
        private val onToggle: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val txtRuta: TextView        = itemView.findViewById(R.id.txtRuta)
        private val txtHorario: TextView     = itemView.findViewById(R.id.txtHorario)
        private val imgRecorrido: ImageView  = itemView.findViewById(R.id.imgRecorrido)
        private val header: LinearLayout     = itemView.findViewById(R.id.headerRuta)
        private val content: LinearLayout    = itemView.findViewById(R.id.contentRuta)
        private val arrow: ImageView         = itemView.findViewById(R.id.imgArrowDown)

        init {
            header.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onToggle(pos)
                }
            }
        }

        fun bind(m: BusRouteModel) {
            txtRuta.text    = "Ruta ${m.code}"
            txtHorario.text = m.schedule
            if (m.isExpanded) {
                content.visibility     = View.VISIBLE
                arrow.setImageResource(R.drawable.ic_arrow_up)
            } else {
                content.visibility     = View.GONE
                arrow.setImageResource(R.drawable.ic_arrow_down)
            }
        }
    }
}
