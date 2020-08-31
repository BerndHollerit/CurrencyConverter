package com.example.paypaycurrencyconverter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.currency_converter.*
import java.math.RoundingMode
import java.net.URL
import java.text.DecimalFormat
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Currency Converter Logic
 */
class CurrencyConverter : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.currency_converter, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currencyMap = currencyValues()

        val spinner: Spinner = view.findViewById(R.id.spinner)

        ArrayAdapter.createFromResource(
                view.context,
                R.array.currency_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val editTextAmount = view.findViewById<EditText>(R.id.editTextAmount)

        // TODO Fix API
        Log.e("d", getString(R.string.fetching_data))
        doAsync {
            val result = URL(getString(R.string.api_url)).readText()
            Log.e(getString(R.string.request), result)
            uiThread {
                Log.e("d", result)
            }
        }

        view.findViewById<Button>(R.id.convert).setOnClickListener { currencyConverterView ->
            var amount = 0.0
            if (editTextAmount != null && editTextAmount.text.toString() != "" && editTextAmount.text.toString() != ".") {
                amount = editTextAmount.text.toString().toDouble()
                Snackbar.make(currencyConverterView, R.string.converting_currency, Snackbar.LENGTH_LONG)
                    .setAction(R.string.converting, null).show()
            }
            else {
                Snackbar.make(currencyConverterView, R.string.enter_amount, Snackbar.LENGTH_LONG)
                    .setAction(R.string.entering, null).show()
            }


            val selected_currency: String = spinner.selectedItem.toString()

            if (currencyMap.get(selected_currency) != null) {

                amount /= currencyMap[selected_currency]!!

            }

            textViewUsd.text = "USD " + currencyConversion(amount, currencyMap["USD"]!!)
            textViewEur.text = "EUR " + currencyConversion(amount, currencyMap["EUR"]!!)
            textViewJpy.text = "JPY " + currencyConversion(amount, currencyMap["JPY"]!!)
            textViewGbp.text = "GBP " + currencyConversion(amount, currencyMap["GBP"]!!)
            textViewAud.text = "AUD " + currencyConversion(amount, currencyMap["AUD"]!!)
            textViewCad.text = "CAD " + currencyConversion(amount, currencyMap["CAD"]!!)
            textViewChf.text = "CHF " + currencyConversion(amount, currencyMap["CHF"]!!)
            textViewCny.text = "CNY " + currencyConversion(amount, currencyMap["CNY"]!!)
            textViewSek.text = "SEK " + currencyConversion(amount, currencyMap["SEK"]!!)
        }
    }

    fun currencyValues(): MutableMap<String, Double> {
        val currencyMap = mutableMapOf("USD" to 1.0)

        currencyMap.put("EUR", 0.847395)
        currencyMap.put("JPY", 105.889499)
        currencyMap.put("GBP", 0.764965)
        currencyMap.put("AUD", 1.394457)
        currencyMap.put("CAD", 1.322395)
        currencyMap.put("CHF", 0.91093)
        currencyMap.put("CNY", 6.919797)
        currencyMap.put("SEK", 8.785803)

        return currencyMap
    }

    fun currencyConversion(value: Double, conversion_rate: Double): String {
        val exchange_rate = value * conversion_rate
        val decimal_format = DecimalFormat("#.##")
        decimal_format.roundingMode = RoundingMode.CEILING
        return decimal_format.format(exchange_rate)
    }
}