package com.abunayla.wattson.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.abunayla.wattson.R
import com.abunayla.wattson.databinding.FragmentPowerCostBinding
import com.abunayla.wattson.helper.PowerCostCalculator
import com.abunayla.wattson.viewmodel.PowerCostViewModel
import com.google.android.material.slider.Slider
import com.sdsmdg.harjot.crollerTest.Croller
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener
import kotlinx.android.synthetic.main.fragment_power_cost.*
import java.text.DecimalFormat

class PowerCostFragment : Fragment() {
    private var _binding: FragmentPowerCostBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: PowerCostViewModel


    private var sbProgressText: String =  "Hour(s) a day: "
    private var localCurrency: String = ""

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

        hCostTxt = getString(R.string.str_h_cost)

        viewModel = ViewModelProvider(this).get(PowerCostViewModel::class.java)

        currentSelection = binding.countryPicker.selectedCountryNameCode

        fetchFreshCostData(viewModel, currentSelection)


        // Country Picker
        binding.countryPicker.setOnCountryChangeListener {
            // Change of country requires new data fetching.
            currentSelection = binding.countryPicker.selectedCountryNameCode
            fetchFreshCostData(viewModel, currentSelection)
        }

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            binding.sbHours.visibility = View.GONE
            binding.ivRotateRight.visibility = View.GONE
            binding.sliderHorizontal.visibility = View.VISIBLE

            binding.sliderHorizontal.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {
                    // Responds to when slider's touch event is being started
                }

                override fun onStopTrackingTouch(slider: Slider) {
                    // Responds to when slider's touch event is being stopped
                    val progressTxt = sbProgressText + slider.value.toInt().toString()
                    tvSeekbarProgress.text = progressTxt
                    // Update number of hours per day to take the progress
                    hoursPerDay = slider.value.toInt()
                    // Recalculate cost data without fetching new data.
                    calculatePowerCost(watts)
                    updateUiItems()
                }
            })

        }
        else{

            binding.sbHours.visibility = View.VISIBLE
            binding.ivRotateRight.visibility = View.VISIBLE
            binding.sliderHorizontal.visibility = View.GONE

            // Hours per day seek bar
            binding.sbHours.setOnCrollerChangeListener(object : OnCrollerChangeListener {
                override fun onProgressChanged(croller: Croller?, progress: Int) {
                    val progressTxt = sbProgressText + progress.toString()
                    tvSeekbarProgress.text = progressTxt
                    // Update number of hours per day to take the progress
                    hoursPerDay = progress
                    // Recalculate cost data without fetching new data.
                    calculatePowerCost(watts)
                    updateUiItems()
                }
                override fun onStartTrackingTouch(croller: Croller?) {}

                override fun onStopTrackingTouch(croller: Croller?) {}
            })
        }


        binding.apply {
            etWatts.editText?.focusAndShowKeyboard()
            etWatts.editText?.doAfterTextChanged {
            if (etWatts.editText?.text?.isNotEmpty()!!) {
                watts = etWatts.editText?.text.toString().toInt()
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
            viewLifecycleOwner, {
                try {
                    cost = it.cost
                    isoCode = it.iso_code
                    currency = it.currency
                    Log.i("TAG", " kWh Cost:$cost for:$isoCode  currency:$currency")
                    calculatePowerCost(watts)
                    updateUiItems()
                    updateLocalCurrencyTV()
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
            //tvHCost.text = hCostTxt.plus(currency + decimalFormat.format(costPerHour))
            //https://stackoverflow.com/questions/55115469/kotlin-android-studio-warning-do-not-concatenate-text-displayed-with-settext-u/55116421
            tvHCost.text = "$hCostTxt $currency ".plus(decimalFormat.format(costPerHour))
            tvDCost.text = decimalFormat.format(costPerDay)
            tvWCost.text = decimalFormat.format(costPerWeek)
            tvMCost.text = decimalFormat.format(costPerMonth)
            tvYCost.text = decimalFormat.format(costPerYear)
        }
    }

    private fun updateLocalCurrencyTV(){
        val holderText = getString(R.string.cost_in_local_currency)
        localCurrency = holderText + currency
        binding.tvCostInLocalCurrency.text = localCurrency
    }

    private fun resetPowerCostUiItems(){
        val decimalFormat = DecimalFormat("#.##")
        tvHCost.text = hCostTxt.plus(decimalFormat.format(0))
        tvDCost.text = decimalFormat.format(0)
        tvWCost.text = decimalFormat.format(0)
        tvMCost.text = decimalFormat.format(0)
        tvYCost.text = decimalFormat.format(0)
        localCurrency = ""
    }


    // Show the soft keyboard and request focus when the fragment launches
    // Source: https://developer.squareup.com/blog/showing-the-android-keyboard-reliably/
    fun View.focusAndShowKeyboard() {
        /**
         * This is to be called when the window already has focus.
         */
        fun View.showTheKeyboardNow() {
            if (isFocused) {
                post {
                    // We still post the call, just in case we are being notified of the windows focus
                    // but InputMethodManager didn't get properly setup yet.
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        requestFocus()
        if (hasWindowFocus()) {
            // No need to wait for the window to get focus.
            showTheKeyboardNow()
        } else {
            // We need to wait until the window gets focus.
            viewTreeObserver.addOnWindowFocusChangeListener(
                object : ViewTreeObserver.OnWindowFocusChangeListener {
                    override fun onWindowFocusChanged(hasFocus: Boolean) {
                        // This notification will arrive just before the InputMethodManager gets set up.
                        if (hasFocus) {
                            this@focusAndShowKeyboard.showTheKeyboardNow()
                            // It’s very important to remove this listener once we are done.
                            viewTreeObserver.removeOnWindowFocusChangeListener(this)
                        }
                    }
                })
        }
    }

}