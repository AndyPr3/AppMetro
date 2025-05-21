package com.upc.appmetropolitano.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.network.SessionManager

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val txtFullname = view.findViewById<TextView>(R.id.txtFullname)
        val txtEmail = view.findViewById<TextInputEditText>(R.id.txtEmail)
        val txtDNI = view.findViewById<TextInputEditText>(R.id.txtDNI)
        val txtPhone = view.findViewById<TextInputEditText>(R.id.txtPhone)

        txtFullname.text = SessionManager.getFullName(view.context)
        txtEmail.setText(SessionManager.getEmail(view.context))
        txtDNI.setText(SessionManager.getDocumentNumber(view.context))
        txtPhone.setText(SessionManager.getPhone(view.context))

    }

    companion object {
        @JvmStatic
        fun newInstance() = InicioFragment()
    }
}