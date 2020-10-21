package com.abunayla.wattson.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
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
import java.text.DecimalFormat


class PowerCostFragment : Fragment() {
    private lateinit var viewModel: PowerCostViewModel
    private var sbProgressText: String = "1"
    private var hCostTxt: String = "NA"
    private var cost: Double = 0.toDouble()
    private var currency: String = ""
    private var isoCode: String = ""
    private var watts: Int = 0
    private var hoursPerDay = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_power_cost, container, false)
        val countryPicker = view.findViewById<CountryCodePicker>(R.id.countryPicker)
        var currentSelection: String// = countryPicker.selectedCountryNameCode
        val sbHoursPerDay = view.findViewById<View>(R.id.sbHours) as Croller
        val wattsInput = view.findViewById<EditText>(R.id.etWatts)


        sbProgressText = getString(R.string.str_seekBar_prog_txt)
        hCostTxt = getString(R.string.str_h_cost)
        viewModel = ViewModelProvider(this).get(PowerCostViewModel::class.java)

        currentSelection = countryPicker.selectedCountryNameCode
        fetchFreshCostData(viewModel, currentSelection)


        // Country Picker
        countryPicker.setOnCountryChangeListener {
            // Change of country requires new data fetching.
            currentSelection = countryPicker.selectedCountryNameCode
            fetchFreshCostData(viewModel, currentSelection)
        }


        // Hours per day seek bar
        sbHoursPerDay.setOnCrollerChangeListener(object : OnCrollerChangeListener{
            override fun onProgressChanged(croller: Croller?, progress: Int) {
                tvSeekbarProgress.text = "$progress" + sbProgressText
                // Update number of hours per day to take the progress
                hoursPerDay = progress
                // Recalculate cost data without fetching new data.
                calculatePowerCost(watts)
            }

            override fun onStartTrackingTouch(croller: Croller?) {}

            override fun onStopTrackingTouch(croller: Croller?) {}
        })

        // Watts input (editText events)
        wattsInput.doAfterTextChanged {
            watts = wattsInput.text.toString().toInt()
            calculatePowerCost(watts)
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun fetchFreshCostData(viewModel: PowerCostViewModel, currentSelection: String) {
        viewModel.readPowerCost(currentSelection).observe(
            viewLifecycleOwner, Observer {
                try {
                    cost = it.cost
                    isoCode = it.iso_code
                    currency = it.currency
                    Log.i("TAG", " kWh Cost:$cost for:$isoCode  currency:$currency")
                    calculatePowerCost(watts)
                } catch (e: Exception) {
                    cost = 0.toDouble()

                    isoCode = ""
                    currency = ""
                    Log.e("TAG", e.message.toString())
                }
            })
    }

    private fun calculatePowerCost(watts: Int) {
        if (watts != 0) {
            val costPerHour = PowerCostCalculator.countHourCost(watts, cost)
            val costPerDay = PowerCostCalculator.countDailyCost(hoursPerDay, costPerHour)
            val costPerWeek = PowerCostCalculator.countWeeklyCost(costPerDay)
            val costPerMonth = PowerCostCalculator.countMonthlyCost(costPerDay)
            val costPerYear = PowerCostCalculator.countYearlyCost(costPerDay)


            val decimalFormat = DecimalFormat("#.##")
            //decimalFormat.roundingMode = RoundingMode.UP

            tvHCost.text = hCostTxt.plus(decimalFormat.format(costPerHour))
            tvDCost.text = decimalFormat.format(costPerDay)
            tvWCost.text = decimalFormat.format(costPerWeek)
            tvMCost.text = decimalFormat.format(costPerMonth)
            tvYCost.text = decimalFormat.format(costPerYear)
        }
    }

}