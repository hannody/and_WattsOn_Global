package com.abunayla.wattson.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abunayla.wattson.R
import com.abunayla.wattson.databinding.FragmentPowerCostBinding
import com.abunayla.wattson.helper.PowerCostCalculator
import com.abunayla.wattson.viewmodel.PowerCostViewModel
import com.sdsmdg.harjot.crollerTest.Croller
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener
import kotlinx.android.synthetic.main.fragment_power_cost.*
import java.text.DecimalFormat

class PowerCostFragment : Fragment() {
    private var _binding: FragmentPowerCostBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: PowerCostViewModel


    private var sbProgressText: String = "1"

    // UI related string holders
    private var hCostTxt: String = "NA"


    // UI related, will take values through the viewModel
    private var cost: Double = 0.toDouble()
    private var currency: String = ""
    private var isoCode: String = ""

    // UI related Var(s) (EditText)/Input
    private var watts: Int = 0

    // UI related Vars (TextViews)
    private var hoursPerDay = 1
    private var costPerHour = 0.toDouble()
    private var costPerDay = 0.toDouble()
    private var costPerWeek = 0.toDouble()
    private var costPerMonth = 0.toDouble()
    private var costPerYear = 0.toDouble()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPowerCostBinding.inflate(inflater, container, false)
        val view = binding.root


        var currentSelection: String// = countryPicker.selectedCountryNameCode




        sbProgressText = getString(R.string.str_seekBar_prog_txt)
        hCostTxt = getString(R.string.str_h_cost)
        viewModel = ViewModelProvider(this).get(PowerCostViewModel::class.java)

        currentSelection = binding.countryPicker.selectedCountryNameCode

        fetchFreshCostData(viewModel, currentSelection)


        // Country Picker
        binding.countryPicker.setOnCountryChangeListener {
            // Change of country requires new data fetching.
            currentSelection = countryPicker.selectedCountryNameCode
            fetchFreshCostData(viewModel, currentSelection)
        }


        // Hours per day seek bar
        binding.sbHours.setOnCrollerChangeListener(object : OnCrollerChangeListener {
            override fun onProgressChanged(croller: Croller?, progress: Int) {
                tvSeekbarProgress.text = "$progress" + sbProgressText
                // Update number of hours per day to take the progress
                hoursPerDay = progress
                // Recalculate cost data without fetching new data.
                calculatePowerCost(watts)
                updateUiItems()
            }

            override fun onStartTrackingTouch(croller: Croller?) {}

            override fun onStopTrackingTouch(croller: Croller?) {}
        })


        //wattsInput.focusAndShowKeyboard()
        binding.etWatts.requestFocus()
        binding.apply {

            etWatts.doAfterTextChanged {
            if (etWatts.text.isNotEmpty()) {
                watts = etWatts.text.toString().toInt()
                calculatePowerCost(watts)
                updateUiItems()
            } else
                resetPowerCostUiItems()
        }
    }// etWatts

        // Inflate the layout for this fragment
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                    updateUiItems()
                } catch (e: Exception) {
                    cost = 0.toDouble()
                    isoCode = ""
                    currency = ""
                    resetPowerCostUiItems()
                    Log.e("TAG", e.message.toString())
                }
            })
    }

    private fun calculatePowerCost(watts: Int) {
        if (watts != 0) {
            costPerHour = PowerCostCalculator.countHourCost(watts, cost)
            costPerDay = PowerCostCalculator.countDailyCost(hoursPerDay, costPerHour)
            costPerWeek = PowerCostCalculator.countWeeklyCost(costPerDay)
            costPerMonth = PowerCostCalculator.countMonthlyCost(costPerDay)
            costPerYear = PowerCostCalculator.countYearlyCost(costPerDay)
        }
    }

    private fun updateUiItems(){

        val decimalFormat = DecimalFormat("#.##")
        //decimalFormat.roundingMode = RoundingMode.UP
        if (watts != 0) {
            tvHCost.text = hCostTxt.plus(decimalFormat.format(costPerHour))
            tvDCost.text = decimalFormat.format(costPerDay)
            tvWCost.text = decimalFormat.format(costPerWeek)
            tvMCost.text = decimalFormat.format(costPerMonth)
            tvYCost.text = decimalFormat.format(costPerYear)
        }
    }

    private fun resetPowerCostUiItems(){
        val decimalFormat = DecimalFormat("#.##")
        tvHCost.text = hCostTxt.plus(decimalFormat.format(0))
        tvDCost.text = decimalFormat.format(0)
        tvWCost.text = decimalFormat.format(0)
        tvMCost.text = decimalFormat.format(0)
        tvYCost.text = decimalFormat.format(0)
    }

}