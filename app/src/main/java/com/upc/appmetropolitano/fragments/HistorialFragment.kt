package com.upc.appmetropolitano.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.activities.MainActivity
import com.upc.appmetropolitano.adapters.MovimientosAdapter
import com.upc.appmetropolitano.network.SessionManager
import com.upc.appmetropolitano.viewmodels.CardViewModel
import java.util.Calendar

class HistorialFragment : Fragment() {

    private lateinit var vm: CardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_historial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireContext()

        val txtCardNumber = view.findViewById<TextView>(R.id.txtCardNumber)
        val spinnerYear = view.findViewById<Spinner>(R.id.spinner_year)
        val spinnerMonth = view.findViewById<Spinner>(R.id.spinner_month)
        val rv = view.findViewById<RecyclerView>(R.id.rv_movements)
        rv.layoutManager = LinearLayoutManager(ctx)

        val cardId = SessionManager.getDefaultCardId(ctx)
        val cardNumber = SessionManager.getCardNumber(ctx)
        txtCardNumber.text = "Tarjeta #$cardNumber"

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear downTo (currentYear - 4)).map { it.toString() }
        val yearAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_item, years).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerYear.adapter = yearAdapter
        spinnerYear.setSelection(0)

        val months = listOf(
            "Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Setiembre","Octubre","Noviembre","Diciembre"
        )
        val monthAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_item, months).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerMonth.adapter = monthAdapter
        spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH))

        vm = ViewModelProvider(this)[CardViewModel::class.java]
        vm.cardHistory.observe(viewLifecycleOwner ){ items ->
            rv.adapter = MovimientosAdapter(items) { mov ->
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        DetalleFragment.newInstance(mov.transactionId)
                    )
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

        val reloadListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val year = spinnerYear.selectedItem.toString().toInt()
                val monthIndex = spinnerMonth.selectedItemPosition + 1
                vm.history(cardId, year, monthIndex)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerYear.onItemSelectedListener = reloadListener
        spinnerMonth.onItemSelectedListener = reloadListener

        val year  = spinnerYear.selectedItem.toString().toInt()
        val month = spinnerMonth.selectedItemPosition + 1
        vm.history(cardId, year, month)
    }

    private fun showLoading(){
        (activity as? MainActivity)?.showLoading()
    }

    private fun hideLoading(){
        (activity as? MainActivity)?.hideLoading()
    }

    companion object {
        @JvmStatic fun newInstance() = HistorialFragment()
    }
}
