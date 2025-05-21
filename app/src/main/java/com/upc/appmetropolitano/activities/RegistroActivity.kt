package com.upc.appmetropolitano.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.viewmodels.AuthViewModel

class RegistroActivity : AppCompatActivity() {

    private lateinit var vm: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val actvDocType = findViewById<AutoCompleteTextView>(R.id.actvDocType)
        val txtDocNumber    = findViewById<TextInputEditText>(R.id.txtDocNumber)
        val txtCardNumber    = findViewById<TextInputEditText>(R.id.txtCardNumber)
        val txtEmail    = findViewById<TextInputEditText>(R.id.txtEmail)
        val txtPassword = findViewById<TextInputEditText>(R.id.txtPassword)

        val btnRegistro = findViewById<Button>(R.id.btnRegistrarme)
        val btnIniciarSesion = findViewById<TextView>(R.id.txtIniciarSesion)

        val tipos = listOf("DNI")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tipos)
        actvDocType.setAdapter(adapter)

        vm = ViewModelProvider(this)[AuthViewModel::class.java]
        vm.saveStatus.observe(this)  { ok ->
            if (ok) successRegistrer()
            else {
                Toast.makeText(this, "Error: Hubo un error al registrarse.", Toast.LENGTH_LONG).show()
            }
        }
        vm.errorMsg.observe(this) { msg ->
            Toast.makeText(this, "Error: $msg", Toast.LENGTH_LONG).show()
        }

        btnRegistro.setOnClickListener{
            if (actvDocType.text.toString().isEmpty() ||
                txtDocNumber.text.toString().isEmpty() ||
                txtCardNumber.text.toString().isEmpty() ||
                txtEmail.text.toString().isEmpty() ||
                txtPassword.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            vm.register(
                actvDocType.text.toString(),
                txtDocNumber.text.toString(),
                txtCardNumber.text.toString(),
                txtEmail.text.toString(),
                txtPassword.text.toString()
            )
        }

        btnIniciarSesion.setOnClickListener{
            goToLogin()
        }

    }

    private fun goToLogin(){
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun successRegistrer(){
        MaterialAlertDialogBuilder(this)
            .setIcon(R.drawable.ic_success)
            .setTitle("Registro completado")
            .setMessage("Usuario registrado existosamente, ya puede iniciar sesión.")
            .setPositiveButton("Aceptar") { dialog, _ ->
                goToLogin()
                dialog.dismiss()
                finish()
            }
            .show()
    }

}