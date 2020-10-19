package com.abunayla.wattson.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abunayla.wattson.R
import com.abunayla.wattson.viewmodel.PowerCostViewModel
import com.hbb20.CountryCodePicker
import com.sdsmdg.harjot.crollerTest.Croller
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener
import kotlinx.android.synthetic.main.fragment_power_cost.*


class PowerCostFragment : Fragment() {
    private lateinit var viewModel: PowerCostViewModel
    private lateinit var sbProgressText: String
    private lateinit var hCostTxt: String
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



        countryPicker.setOnCountryChangeListener {
            currentSelection = countryPicker.selectedCountryNameCode
            dataIntegrityCheckAndSet(viewModel, currentSelection)
        }


        sbHoursPerDay.setOnCrollerChangeListener(object : OnCrollerChangeListener{
            override fun onProgressChanged(croller: Croller?, progress: Int) {
                tvSeekbarProgress.text = "$progress" + sbProgressText
                // Update number of hours per day to take the progress
                hoursPerDay = progress
            }

            override fun onStartTrackingTouch(croller: Croller?) {}

            override fun onStopTrackingTouch(croller: Croller?) {}
        })


        wattsInput.setOnClickListener {
            watts = wattsInput.text.toString().toInt()
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun dataIntegrityCheckAndSet(viewModel: PowerCostViewModel, currentSelection: String) {
        viewModel.readPowerCost(currentSelection).observe(
            viewLifecycleOwner, Observer {
                try {
                    cost = it.cost
                    isoCode = it.iso_code
                    currency = it.currency
                    Log.i("TAG", "$cost $isoCode $currency $watts $hoursPerDay")
                } catch (e: Exception) {
                    cost = 0.toDouble()

                    isoCode = ""
                    currency = ""
                    Log.e("TAG", e.message.toString())
                }
            })
    }

}