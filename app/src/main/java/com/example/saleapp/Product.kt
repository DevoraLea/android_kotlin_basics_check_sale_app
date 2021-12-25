package com.example.saleapp

import kotlinx.android.synthetic.main.activity_main.*

data class Product(var price:Double,var percentage:Double) {

    fun priceAfterSale(): Double {
        return price - (price * percentage)
    }
    fun priceFormatted(){

    }
}