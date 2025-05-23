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
import com.upc.appmetropolitano.activities.MainActivity
import com.upc.appmetropolitano.adapters.MovimientosAdapter
import com.upc.appmetropolitano.network.SessionManager
import com.upc.appmetropolitano.viewmodels.CardViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        val txtUltimosMov = view.findViewById<TextView>(R.id.txtUltimosMov)
        val txtSaldo = view.findViewById<TextView>(R.id.txtSaldo)

        txtUltimosMov.text = "Últimos movimientos (${getMonthName()})"

        vm = ViewModelProvider(this)[CardViewModel::class.java]
        vm.cardInfo.observe(viewLifecycleOwner ){ card ->
            val rv = view.findViewById<RecyclerView>(R.id.rv_movimientos)
            rv.layoutManager = LinearLayoutManager(view.context)

            txtCardNumber.text = "Tarjeta #${card.cardNumber}"
            txtSaldo.text = "S/${card.balance.toString()}"

            rv.adapter = MovimientosAdapter(card.history) { mov ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DetalleFragment.newInstance(mov.transactionId))
                    .addToBackStack(null)
                    .commit()
            }
        }
        vm.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading()
            else hideLoading()
        }
        vm.errorMsg.observe(viewLifecycleOwner ) { msg ->
            Toast.makeText(view.context, "Error: $msg", Toast.LENGTH_LONG).show()
        }

        if (defaultCardId != -1) {
            vm.info(userId, defaultCardId)
        }
    }

    private fun getMonthName(): String{
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("MMMM", Locale("es", "ES"))
        return sdf.format(calendar.time)
    }

    private fun showLoading(){
        (activity as? MainActivity)?.showLoading()
    }

    private fun hideLoading(){
        (activity as? MainActivity)?.hideLoading()
    }

    companion object {
        @JvmStatic
        fun newInstance() = InicioFragment()
    }
}