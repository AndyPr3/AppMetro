package com.upc.appmetropolitano.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.upc.appmetropolitano.R


class AjustesFragment : Fragment() {

    private lateinit var btnGuardar: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ajustes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnGuardar = view.findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            successAjutes(view)
        }

    }

    private fun successAjutes(root: View){
        var dialog = MaterialAlertDialogBuilder(root.context)
            .setIcon(R.drawable.ic_success)
            .setCancelable(false)
            .setTitle("Ajustes guardados")
            .setMessage("Los ajustes se han guardado correctamente.")
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
        dialog.setCanceledOnTouchOutside(false)
    }

    companion object {
        @JvmStatic fun newInstance() = AjustesFragment()
    }
}