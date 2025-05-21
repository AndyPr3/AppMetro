package com.upc.appmetropolitano.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.adapters.MovimientosAdapter
import com.upc.appmetropolitano.network.SessionManager
import com.upc.appmetropolitano.viewmodels.CardViewModel

class InicioFragment : Fragment() {

    private lateinit var vm: CardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val defaultCardId = SessionManager.getDefaultCardId(requireContext())
        val userId = SessionManager.getUserId(requireContext())

        val txtCardNumber = view.findViewById<TextView>(R.id.txtCardNumber)
        val txtSaldo = view.findViewById<TextView>(R.id.txtSaldo)

        vm = ViewModelProvider(this)[CardViewModel::class.java]
        vm.cardInfo.observe(viewLifecycleOwner ){ card ->
            val rv = view.findViewById<RecyclerView>(R.id.rv_movimientos)
            rv.layoutManager = LinearLayoutManager(view.context)

            txtCardNumber.text = card.cardNumber
            txtSaldo.text = "S/${card.balance.toString()}"

            rv.adapter = MovimientosAdapter(card.history) { mov ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DetalleFragment.newInstance(mov.transactionId))
                    .addToBackStack(null)
                    .commit()
            }
        }
        vm.errorMsg.observe(viewLifecycleOwner ) { msg ->
            Toast.makeText(view.context, "Error: $msg", Toast.LENGTH_LONG).show()
        }

        if (defaultCardId != -1) {
            vm.info(userId, defaultCardId)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = InicioFragment()
    }
}