package com.upc.appmetropolitano.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat

import androidx.drawerlayout.widget.DrawerLayout

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.fragments.AjustesFragment
import com.upc.appmetropolitano.fragments.HistorialFragment
import com.upc.appmetropolitano.fragments.InicioFragment
import com.upc.appmetropolitano.fragments.PerfilFragment
import com.upc.appmetropolitano.fragments.PoliticasFragment
import com.upc.appmetropolitano.fragments.RecargaFragment
import com.upc.appmetropolitano.fragments.RutasFragment
import com.upc.appmetropolitano.network.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar      = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView      = findViewById(R.id.nav_view)
        bottomNav    = findViewById(R.id.bottom_nav)

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InicioFragment())
                .commit()
        }

        val name = SessionManager.getFirtsName(this)
        toolbar.title = "Hola, $name"

        bottomNav.selectedItemId = R.id.nav_inicio
        setupBottomNav()
        setupDrawerNav()


    }

    private fun setupBottomNav() {
        bottomNav.setOnItemSelectedListener { item ->
            val name = SessionManager.getFirtsName(this)
            toolbar.title = if (item.title =="Inicio") "Hola $name" else item.title
            val fragment = when (item.itemId) {
                R.id.nav_inicio -> InicioFragment()
                R.id.nav_recarga -> RecargaFragment()
                R.id.nav_historial -> HistorialFragment()
                else               -> null
            }
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
                true
            } ?: false
        }

        bottomNav.selectedItemId = R.id.nav_inicio
    }


    private fun setupDrawerNav() {
        navView.setNavigationItemSelectedListener { menuItem ->

            drawerLayout.closeDrawer(GravityCompat.START)

            val fragment = when(menuItem.itemId) {
                R.id.nav_profile -> PerfilFragment()
                R.id.nav_settings -> AjustesFragment()
                R.id.nav_privacy -> PoliticasFragment()
                R.id.nav_bus_routes -> RutasFragment()
                R.id.nav_logout -> {

                    MaterialAlertDialogBuilder(this)
                        .setTitle("Cerrar sesión")
                        .setMessage("¿Estás seguro que deseas cerrar sesión?")
                        .setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Aceptar") { _, _ ->
                            SessionManager.clearSession(this)
                            val intent = Intent(this, LoginActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                            finish()
                        }
                        .show()

                    null
                }
                else                     -> null
            }

            fragment?.let {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .addToBackStack(null)
                    .commit()

                toolbar.title = menuItem.title
                supportActionBar?.title = menuItem.title
            }

            true
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}