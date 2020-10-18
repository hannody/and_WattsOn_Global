package com.abunayla.wattson.ui.fragments

import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abunayla.wattson.R
import com.abunayla.wattson.helper.PowerCostCalculator
import com.abunayla.wattson.viewmodel.PowerCostViewModel
import com.hbb20.CountryCodePicker
import com.sdsmdg.harjot.crollerTest.Croller
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener

import kotlinx.android.synthetic.main.fragment_power_cost.*

class PowerCostFragment : Fragment() {
    private val TAG = "PowerCostFragment"
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

        dataIntegrityCheckAndSet(viewModel, currentSelection)

        countryPicker.setOnCountryChangeListener {
            currentSelection = countryPicker.selectedCountryNameCode
            dataIntegrityCheckAndSet(viewModel, currentSelection)
        }

        val sbHoursPerDay = view.findViewById<View>(R.id.sbHours) as Croller

        sbHoursPerDay.setOnCrollerChangeListener(object : OnCrollerChangeListener{
            override fun onProgressChanged(croller: Croller?, progress: Int) {
                tvHoursPerDay.text = "$progress  Hour(s) A Day"
            }

            override fun onStartTrackingTouch(croller: Croller?) {}

            override fun onStopTrackingTouch(croller: Croller?) {}
        })


        return view
    }

    private fun dataIntegrityCheckAndSet(viewModel: PowerCostViewModel, currentSelection: String) {
        viewModel.readPowerCost(currentSelection).observe(
            viewLifecycleOwner, Observer {
//                try {
//                    var h = PowerCostCalculator().countHourCost(etWatts.text.toString().toInt(), it.cost)
//                    tvHourlyCost.text = h.toString()
//
//                } catch (e: Exception) {
//                    Log.e(TAG, e.message.toString())
//                }
            })
    }

}