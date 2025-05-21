package com.upc.appmetropolitano.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.activities.MainActivity
import com.upc.appmetropolitano.viewmodels.TransactionViewModel

class DetalleFragment : Fragment(R.layout.fragment_detalle) {

    private lateinit var vm: TransactionViewModel

    companion object {
        private const val ARG_TRANSACTION_ID = "arg_transaction_id"

        @JvmStatic
        fun newInstance(transactionId: Int) =
            DetalleFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TRANSACTION_ID, transactionId)
                }
            }
    }

    private var transactionId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionId = requireArguments().getInt(ARG_TRANSACTION_ID, -1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.supportActionBar?.title = "Detalle de movimiento"

        val txtCardNumber = view.findViewById<TextView>(R.id.txtCardNumber)
        val txtFecha = view.findViewById<TextView>(R.id.txtFecha)
        val txtSaldo = view.findViewById<TextView>(R.id.txtSaldo)
        val txtEstacion = view.findViewById<TextView>(R.id.txtEstacion)
        val lblEstacion = view.findViewById<TextView>(R.id.lblEstacion)
        val cardMap = view.findViewById<MaterialCardView>(R.id.card_map_detail)


        vm = ViewModelProvider(this)[TransactionViewModel::class.java]
        vm.responseDetail.observe(viewLifecycleOwner ){ item ->
            if(item.type == "R"){ // Recarga
                cardMap.isVisible = false
                txtSaldo.setTextColor(Color.BLACK)
                lblEstacion.text = "Método de pago"
                txtEstacion.text =  if (item.paymentMethodId == 1) "Tarjeta de crédito" else "Yape"
            } else { // Viaje
                cardMap.isVisible = true
                txtSaldo.setTextColor(Color.RED)
                lblEstacion.text = "Estación"
                txtEstacion.text = item.station
            }
            txtCardNumber.text = "Tarjeta #${item.cardNumber}"
            txtSaldo.text = "S/${item.amount}"
            txtFecha.text = "${item.transactionDate}"
        }
        vm.errorMsg.observe(viewLifecycleOwner ) { msg ->
            Toast.makeText(view.context, "Error: $msg", Toast.LENGTH_LONG).show()
        }

        if (transactionId != -1) {
            vm.detail(transactionId)
        } else {
            Toast.makeText(requireContext(), "Movimiento inválido", Toast.LENGTH_SHORT).show()
        }
    }

}
