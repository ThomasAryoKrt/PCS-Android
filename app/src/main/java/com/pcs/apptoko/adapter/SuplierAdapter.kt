package com.pcs.apptoko.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pcs.apptoko.LoginActivity
import com.pcs.apptoko.R
import com.pcs.apptoko.api.BaseRetrofit
import com.pcs.apptoko.response.produk.ProdukResponsePost
import com.pcs.apptoko.response.suplier.ProdukSup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("NotifyDataSetChanged")
class SuplierAdapter() : RecyclerView.Adapter<SuplierAdapter.ViewHolder>() {

    private val api by lazy { BaseRetrofit().endPoint }
    private val listSuplier = mutableListOf<ProdukSup>()

    fun setSuplier(listSuplier: List<ProdukSup>) {
        this.listSuplier.clear()
        this.listSuplier.addAll(listSuplier)
        notifyDataSetChanged()
    }

    fun sortNama(asc: Boolean) {
        if (asc) {
            listSuplier.sortBy { produk -> produk.nama }
        } else {
            listSuplier.sortByDescending { produk -> produk.nama }
        }
        notifyDataSetChanged()
    }

    fun sortHarga(asc: Boolean) {
        if (asc) {
            listSuplier.sortBy { produk -> produk.harga.toInt() }
        } else {
            listSuplier.sortByDescending { produk -> produk.harga.toInt() }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_suplier, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produksup = listSuplier[position]
        holder.txtNamaProduk.text = produksup.nama
        holder.txtHarga.text = "Rp. ${produksup.harga}"

        val token = LoginActivity.sessionManager.getString("TOKEN")

        holder.btnAddSup.setOnClickListener {
            Toast.makeText(holder.itemView.context, produksup.nama.toString(), Toast.LENGTH_LONG)
                .show()
            val bundle = Bundle()
            bundle.putParcelable("produksup", produksup)
            bundle.putString("status", "beli")

            holder.itemView.findNavController().navigate(R.id.produkFormFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return listSuplier.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNamaProduk = itemView.findViewById(R.id.txtNamaProduk) as TextView
        val txtHarga = itemView.findViewById(R.id.txtHarga) as TextView
        val btnAddSup = itemView.findViewById(R.id.btnAddSup) as ImageButton
//        val btnDelete = itemView.findViewById(R.id.btnDelete) as ImageButton
    }
}