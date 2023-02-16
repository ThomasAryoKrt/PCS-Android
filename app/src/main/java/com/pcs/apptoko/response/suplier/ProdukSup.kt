package com.pcs.apptoko.response.suplier

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProdukSup(
    val admin_id: String,
    val harga: String,
    val id: String,
    val nama: String,
    val nama_admin: String,
    val kategori: String,
    val stok: String
):Parcelable