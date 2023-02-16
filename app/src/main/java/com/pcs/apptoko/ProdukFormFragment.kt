package com.pcs.apptoko

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.pcs.apptoko.api.BaseRetrofit
import com.pcs.apptoko.response.login.LoginResponse
import com.pcs.apptoko.response.produk.Produk
import com.pcs.apptoko.response.produk.ProdukResponsePost
import com.pcs.apptoko.response.suplier.ProdukSup
import com.pcs.apptoko.response.suplier.ProdukSupResponse
import com.pcs.apptoko.response.suplier.ProdukSupResponsePost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdukFormFragment : Fragment() {

    private val api by lazy { BaseRetrofit().endPoint}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_produk_form, container, false)

        val btnProsesProduk = view.findViewById<Button>(R.id.btnProsesProduk)

        val txtFormNama = view.findViewById<TextView>(R.id.txtFormNama)
        val txtFormHarga = view.findViewById<TextView>(R.id.txtFormHarga)
        val txtFormStok = view.findViewById<TextView>(R.id.txtFormStok)

        val status = arguments?.getString("status")
        val produk = arguments?.getParcelable<Produk>("produk")
        val produksup = arguments?.getParcelable<ProdukSup>("produksup")

        Log.d("produkForm", produk.toString())

        if(status=="edit"){
            txtFormNama.setText(produk?.nama.toString())
            txtFormHarga.setText(produk?.harga.toString())
            txtFormStok.setText(produk?.stok.toString())
        }else if(status=="beli"){
            txtFormNama.setText(produksup?.nama.toString())
            txtFormHarga.setText(produksup?.harga.toString())
            txtFormStok.setText(produksup?.stok.toString())
        }

        btnProsesProduk.setOnClickListener {
            val txtFormNama = view.findViewById<TextInputEditText>(R.id.txtFormNama)
            val txtFormHarga = view.findViewById<TextInputEditText>(R.id.txtFormHarga)
            val txtFormStok = view.findViewById<TextInputEditText>(R.id.txtFormStok)

            val token = LoginActivity.sessionManager.getString("TOKEN")
            val adminId = LoginActivity.sessionManager.getString("ADMIN_ID")

            if(status=="edit"){
                api.putProduk(token.toString(),produk?.id.toString().toInt(),adminId.toString().toInt(),txtFormNama.text.toString(),txtFormHarga.text.toString().toInt(),txtFormStok.text.toString().toInt()).enqueue(object :
                    Callback<ProdukResponsePost> {
                    override fun onResponse(
                        call: Call<ProdukResponsePost>,
                        response: Response<ProdukResponsePost>,
                    ) {
                        Log.d("ResponData", response.body()!!.data.toString())
                        Toast.makeText(activity?.applicationContext, "Data "+ response.body()!!.data.produk.nama.toString() +" di Edit",Toast.LENGTH_LONG).show()

                        findNavController().navigate(R.id.produkFragment)
                    }

                    override fun onFailure(call: Call<ProdukResponsePost>, t: Throwable) {
                        Log.e("Error", t.toString())
                    }

                })
            }else if(status=="beli"){
            api.putProdukSup(token.toString(),produksup?.id.toString().toInt(),adminId.toString().toInt(),txtFormNama.text.toString(),txtFormHarga.text.toString().toInt(),txtFormStok.text.toString().toInt()).enqueue(object :
                Callback<ProdukSupResponsePost> {
                override fun onResponse(
                    call: Call<ProdukSupResponsePost>,
                    response: Response<ProdukSupResponsePost>,
                ) {
                    Log.d("ResponData", response.body()!!.data.toString())
                    Toast.makeText(activity?.applicationContext, "Data "+ response.body()!!.data.produksup.nama.toString() +" berhasil di Beli",Toast.LENGTH_LONG).show()

                    findNavController().navigate(R.id.suplierFragment)
                }

                override fun onFailure(call: Call<ProdukSupResponsePost>, t: Throwable) {
                    Log.e("Error", t.toString())
                }

            })
                api.postProdukSup(token.toString(),adminId.toString().toInt(),txtFormNama.text.toString(),txtFormHarga.text.toString().toInt(),txtFormStok.text.toString().toInt()).enqueue(object :
                    Callback<ProdukSupResponsePost> {
                    override fun onResponse(
                        call: Call<ProdukSupResponsePost>,
                        response: Response<ProdukSupResponsePost>,
                    ) {
//                        Log.d("Data", response.toString())
//                        Toast.makeText(activity?.applicationContext, "Data di input ke Suplier",Toast.LENGTH_LONG).show()

                        findNavController().navigate(R.id.suplierFragment)
                    }

                    override fun onFailure(call: Call<ProdukSupResponsePost>, t: Throwable) {
                        Log.e("Error", t.toString())
                    }

                })
        }else if (status=="tambahsup"){
                api.postProdukSup(token.toString(),adminId.toString().toInt(),txtFormNama.text.toString(),txtFormHarga.text.toString().toInt(),txtFormStok.text.toString().toInt()).enqueue(object :
                    Callback<ProdukSupResponsePost> {
                    override fun onResponse(
                        call: Call<ProdukSupResponsePost>,
                        response: Response<ProdukSupResponsePost>,
                    ) {
                        Log.d("Data", response.toString())
                        Toast.makeText(activity?.applicationContext, "Data di input ke Suplier",Toast.LENGTH_LONG).show()

                        findNavController().navigate(R.id.suplierFragment)
                    }

                    override fun onFailure(call: Call<ProdukSupResponsePost>, t: Throwable) {
                        Log.e("Error", t.toString())
                    }

                })
            }else{
                api.postProduk(token.toString(),adminId.toString().toInt(),txtFormNama.text.toString(),txtFormHarga.text.toString().toInt(),txtFormStok.text.toString().toInt()).enqueue(object :
                    Callback<ProdukResponsePost> {
                    override fun onResponse(
                        call: Call<ProdukResponsePost>,
                        response: Response<ProdukResponsePost>,
                    ) {
                        Log.d("Data", response.toString())
                        Toast.makeText(activity?.applicationContext, "Data di input ke Produk",Toast.LENGTH_LONG).show()

                        findNavController().navigate(R.id.produkFragment)
                    }

                    override fun onFailure(call: Call<ProdukResponsePost>, t: Throwable) {
                        Log.e("Error", t.toString())
                    }

                })
            }

        }

        return view
    }
}