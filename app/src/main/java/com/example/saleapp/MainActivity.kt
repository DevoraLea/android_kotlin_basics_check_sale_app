package com.example.saleapp

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import com.example.saleapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException
import java.text.NumberFormat
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var product = Product(0.0,0.1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCheck.setOnClickListener(View.OnClickListener { checkTheFinalPrice()
             })
        binding.editPriceProduct.setOnKeyListener { v, keyCode, _ -> handleKeyEvent(v,keyCode)}
        binding.editPersonalite.setOnKeyListener { v, keyCode, _ -> handleKeyEvent(v,keyCode)}
        binding.groupSale.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.radioPersonalite){
                binding.editPersonaliteLayout.visibility = View.VISIBLE
                layoutPersonalizate.visibility = View.VISIBLE
            }else{
                hidePersonalizate()
                when (binding.groupSale.checkedRadioButtonId) {
                    R.id.radio_ten_percent -> product.percentage = 0.1
                    R.id.radio_thrteen_percent -> product.percentage = 0.3
                    R.id.radio_seventeen -> product.percentage = 0.7
                    else -> 0
                }
            }
        }
        editPersonalite.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(checkNotEmptyOrInvalidValues(editPersonalite)) {
                    product.percentage = (editPersonalite.text.toString().toDouble())/100
                }else{
                    showSnackBar("insert an correct value")
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
    }

    private fun checkTheSale():Boolean {
        if(binding.groupSale.checkedRadioButtonId == R.id.radioPersonalite){
            if(!checkNotEmptyOrInvalidValues(editPersonalite)){
                showSnackBar("please insert the percentage of the sale")
                return false
            }
            else{
                product.percentage = (editPersonalite.text.toString().toDouble())/100
            }
        }
        return true
    }

    private fun hidePersonalizate() {
        layoutPersonalizate.visibility = View.GONE
    }

    fun showSnackBar(text: String) {
        Snackbar.make(findViewById(R.id.main_activity), text, Snackbar.LENGTH_SHORT).show()
    }

    fun checkTheFinalPrice() {
        if(checkTheSale()){
        if (checkNotEmptyOrInvalidValues(editPriceProduct)) {
            product.price = editPriceProduct.text.toString().toDouble()
            showDialog()
        } else if(!checkNotEmptyOrInvalidValues(editPriceProduct)) {
            showSnackBar(getString(R.string.enter_valid_price))
        }else if(!checkNotEmptyOrInvalidValues(editPersonalite)) {
            showSnackBar("enter the sale")
        }}
    }
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
    fun checkNotEmptyOrInvalidValues(editText: EditText): Boolean {
        if (editText.text.isNotEmpty()) {
            try{
                editText.text.toString().toDouble()
            }catch (exc: NumberFormatException){
                return false
            }
            return true
        }
        return false
    }

    fun showDialog() {
        var dialog = AlertDialog.Builder(this)
        val priceFormatted = NumberFormat.getCurrencyInstance().format(product.priceAfterSale())
        dialog.setTitle(getString(R.string.final_price,priceFormatted))
        dialog.setIcon(R.drawable.the_sale)
        dialog.setPositiveButton(getString(R.string.ok), DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        dialog.setMessage(getString(R.string.good_deal))
        dialog.show()
    }
}