package com.pcs.apptoko.response.suplier

data class ProdukSupResponsePost (
    val `data`: DataProdukSup,
    val message: String,
    val success: Boolean
    )

data class DataProdukSup (
    val `produksup`: ProdukSup
)