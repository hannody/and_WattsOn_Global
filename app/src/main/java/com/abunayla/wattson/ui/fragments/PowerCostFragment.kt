package com.abunayla.wattson.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abunayla.wattson.R
import com.abunayla.wattson.viewmodel.PowerCostViewModel
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.fragment_power_cost.*

class PowerCostFragment : Fragment() {
    private lateinit var viewModel: PowerCostViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_power_cost, container, false)
        val countryPicker = view.findViewById<CountryCodePicker>(R.id.countryPicker)
        var currentSelection: String = countryPicker.selectedCountryNameCode
        viewModel = ViewModelProvider(this).get(PowerCostViewModel::class.java)

        viewModel.readPowerCost(currentSelection).observe(
            viewLifecycleOwner, Observer {
                tvShowCost.text = it.first().cost.toString()
                tvShowCurrency.text = it.first().currency
                tvshowIsoCode.text = it.first().iso_code
            })


        countryPicker.setOnCountryChangeListener {
            currentSelection = countryPicker.selectedCountryNameCode
            viewModel.readPowerCost(currentSelection).observe(
                viewLifecycleOwner, Observer {
                    tvShowCost.text = it.first().cost.toString()
                    tvShowCurrency.text = it.first().currency
                    tvshowIsoCode.text = it.first().iso_code
                })

        }

        return view
    }

}