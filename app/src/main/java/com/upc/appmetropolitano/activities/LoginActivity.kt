package com.upc.appmetropolitano.activities
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.network.SessionManager
import com.upc.appmetropolitano.viewmodels.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var vm: AuthViewModel
    private lateinit var loading: CircularProgressIndicator
    private lateinit var btnIniciar:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        loading = findViewById(R.id.loading)
        btnIniciar = findViewById(R.id.btnIniciarSesion)

        val txtEmail    = findViewById<TextInputEditText>(R.id.txtEmail)
        val txtPassword = findViewById<TextInputEditText>(R.id.txtPassword)
        val txtRegistrar = findViewById<TextView>(R.id.txtRegistrar)

        vm = ViewModelProvider(this)[AuthViewModel::class.java]
        vm.user.observe(this){ user ->
            SessionManager.saveSession(
                this,
                user.userId,
                user.email,
                user.firtsName,
                user.fullName,
                user.documentNumber,
                user.phone,
                user.cardNumber,
                user.defaultCardId
            )
            Log.i("LOGIN", "Usuario logueado: $user")
            goToMain()
        }
        vm.loading.observe(this) { isLoading ->
            if (isLoading) showLoading()
            else hideLoading()
        }
        vm.errorMsg.observe(this) { msg ->
            Toast.makeText(this, "Error: $msg", Toast.LENGTH_LONG).show()
        }

        txtRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        btnIniciar.setOnClickListener {
            val email = txtEmail.text.toString().trim()
            val pass  = txtPassword.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.login(email, pass)
        }
    }

    private fun goToMain(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(){
        loading.visibility = View.VISIBLE
        btnIniciar.isEnabled = false
    }

    private fun hideLoading(){
        loading.visibility = View.GONE
        btnIniciar.isEnabled = true
    }

}
