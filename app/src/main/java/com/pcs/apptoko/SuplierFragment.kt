package com.pcs.apptoko

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pcs.apptoko.LoginActivity.Companion.sessionManager
import com.pcs.apptoko.adapter.SuplierAdapter
import com.pcs.apptoko.api.BaseRetrofit
import com.pcs.apptoko.response.suplier.ProdukSup
import com.pcs.apptoko.response.suplier.ProdukSupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuplierFragment : Fragment() {

    private val api by lazy { BaseRetrofit().endPoint }
    private val rvAdapter = SuplierAdapter()
    private val listSuplier = mutableListOf<ProdukSup>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_suplier, container, false)

        val rv = view.findViewById(R.id.rv_suplier) as RecyclerView
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = rvAdapter

        getProdukSup(view)

        val btnTambahSup = view.findViewById<Button>(R.id.btnTambahSup)
        btnTambahSup.setOnClickListener {
            Toast.makeText(activity?.applicationContext, "Click", Toast.LENGTH_LONG).show()

            val bundle = Bundle()
            bundle.putString("status", "tambahsup")

            findNavController().navigate(R.id.produkFormFragment, bundle)
        }

        // search
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    rvAdapter.setSuplier(listSuplier)
                } else {
                    rvAdapter.setSuplier(listSuplier.filter { p ->
                        p.nama.contains(
                            newText,
                            ignoreCase = true
                        )
                    })
                }
                return true
            }
        })

        //sorting
        val spinner = view.findViewById<Spinner>(R.id.spinnerSort)
        val sorts = resources.getStringArray(R.array.spinner_sort)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, sorts)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> rvAdapter.sortNama(true)
                    1 -> rvAdapter.sortNama(false)
                    2 -> rvAdapter.sortHarga(true)
                    3 -> rvAdapter.sortHarga(false)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        return view
    }

    fun getProdukSup(view: View) {
        val token = sessionManager.getString("TOKEN")

        api.getProdukSup(token.toString()).enqueue(object : Callback<ProdukSupResponse> {
            override fun onResponse(
                call: Call<ProdukSupResponse>,
                response: Response<ProdukSupResponse>
            ) {
                Log.d("ProdukData", response.body().toString())

                response.body()?.data?.produksup?.let {
                    val txtTotalProduk = view.findViewById(R.id.txtTotalProduk) as TextView
                    txtTotalProduk.text = it.size.toString() + " Item"

                    listSuplier.clear()
                    listSuplier.addAll(it)
                    rvAdapter.setSuplier(it)
                    rvAdapter.sortNama(true)
                }
            }

            override fun onFailure(call: Call<ProdukSupResponse>, t: Throwable) {
                Log.e("ProdukError", t.toString())
            }

        })
    }

}