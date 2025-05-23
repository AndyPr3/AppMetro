package com.upc.appmetropolitano.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.appmetropolitano.R
import com.upc.appmetropolitano.activities.MainActivity
import com.upc.appmetropolitano.adapters.RutasAdapter
import com.upc.appmetropolitano.models.BusRouteModel
import com.upc.appmetropolitano.viewmodels.BusRouteViewModel

class RutasFragment : Fragment() {
    private val rutas = mutableListOf<BusRouteModel>()
    private lateinit var adapter: RutasAdapter
    private lateinit var vm: BusRouteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rutas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rv_rutas)
        rv.layoutManager = LinearLayoutManager(view.context)
        adapter = RutasAdapter(rutas) { pos ->
            rutas[pos].isExpanded = !rutas[pos].isExpanded
            adapter.notifyItemChanged(pos)
        }
        rv.adapter = adapter

        vm = ViewModelProvider(this)[BusRouteViewModel::class.java]
        vm.responseBus.observe(viewLifecycleOwner ){ items ->
            rutas.clear()
            for (item in items) {
                rutas += item
            }
            adapter.notifyDataSetChanged()
        }
        vm.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading()
            else hideLoading()
        }
        vm.errorMsg.observe(viewLifecycleOwner ) { msg ->
            Toast.makeText(view.context, "Error: $msg", Toast.LENGTH_LONG).show()
        }

        vm.routes()


    }

    private fun showLoading(){
        (activity as? MainActivity)?.showLoading()
    }

    private fun hideLoading(){
        (activity as? MainActivity)?.hideLoading()
    }

    companion object {
        @JvmStatic fun newInstance() = RutasFragment()
    }
}