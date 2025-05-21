package com.upc.appmetropolitano.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.network.SessionManager
import com.upc.appmetropolitano.viewmodels.TransactionViewModel
class RecargaFragment : Fragment() {

    private lateinit var rbCard: MaterialRadioButton
    private lateinit var rbYape: MaterialRadioButton
    private lateinit var txtCardNumber :TextView
    private lateinit var txtMonto :TextInputEditText
    private lateinit var vm: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recarga, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardNumber = SessionManager.getCardNumber(requireContext())

        rbCard = view.findViewById(R.id.rbCard)
        rbYape = view.findViewById(R.id.rbYape)

        rbCard.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) rbYape.isChecked = false
        }
        rbYape.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) rbCard.isChecked = false
        }

        val btnPay = view.findViewById<Button>(R.id.btnPagar)
        txtMonto = view.findViewById(R.id.txtMonto)

        txtCardNumber = view.findViewById(R.id.txtCardNumber)

        txtCardNumber.text = "Tarjeta : #$cardNumber"

        vm = ViewModelProvider(this)[TransactionViewModel::class.java]
        vm.saveStatus.observe(viewLifecycleOwner ){ ok ->
            if (ok) successRecarga(view)
            else
                Toast.makeText(view.context, "Error: No se pude realizar la recarga.", Toast.LENGTH_LONG).show()
        }
        vm.errorMsg.observe(viewLifecycleOwner ) { msg ->
            Toast.makeText(view.context, "Error: $msg", Toast.LENGTH_LONG).show()
        }


        btnPay.setOnClickListener {
            val amountText = txtMonto.text.toString().trim()
            if (amountText.isEmpty() || amountText == "0") {
                txtMonto.error = "Ingresa un monto"
                return@setOnClickListener
            }

            val selectedMethod = when {
                rbCard.isChecked -> 1 //Tarjeta
                rbYape.isChecked -> 2 //Yape
                else -> 0
            }

            vm.register(cardNumber, selectedMethod, txtMonto.text.toString().toDouble(), "R", "" )

        }
    }

    private fun limpiar(){
        txtMonto.setText("")
        rbCard.isChecked = true
        rbYape.isChecked = false
    }

    private fun successRecarga(root: View){
        var dialog = MaterialAlertDialogBuilder(root.context)
            .setIcon(R.drawable.ic_success)
            .setCancelable(false)
            .setTitle("Recarga exitosa")
            .setMessage("Su recarga se ha procesado correctamente.")
            .setPositiveButton("Aceptar") { dialog, _ ->
                limpiar()
                dialog.dismiss()
            }
            .show()
        dialog.setCanceledOnTouchOutside(false)
    }

}