package com.upc.appmetro.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.upc.appmetro.R
import com.upc.appmetro.ui.inicio.InicioActivity
import com.upc.appmetro.ui.registro.RegistroActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        btnIniciarSesion.setOnClickListener{
            var intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }

        val btnRegistro = findViewById<TextView>(R.id.textRegistrar)
        btnRegistro.setOnClickListener{
            var intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}