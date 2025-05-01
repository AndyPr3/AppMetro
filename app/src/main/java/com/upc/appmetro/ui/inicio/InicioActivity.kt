package com.upc.appmetro.ui.inicio

import com.upc.appmetro.data.model.Movimiento
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.appmetro.R

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lista = listOf(
            Movimiento("19 Jun 2024 - 7:55 am", 3.50),
            Movimiento("19 Jun 2024 - 7:55 am", 3.50),
            Movimiento("19 Jun 2024 - 7:55 am", 3.50),
            Movimiento("19 Jun 2024 - 7:55 am", -3.50), // Este será rojo
            Movimiento("19 Jun 2024 - 7:55 am", 3.50)
        )

        val adapter = MovimientoAdapter(lista)
        val recyclerView = findViewById<RecyclerView>(R.id.transactions_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}