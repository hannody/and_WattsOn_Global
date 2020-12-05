package com.abunayla.wattson.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abunayla.wattson.db.PowerCost
import com.abunayla.wattson.db.PowerCostDatabase
import com.abunayla.wattson.helper.PowerCostCalculator
import com.abunayla.wattson.repository.PowerCostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PowerCostViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PowerCostRepository
    val watts = MutableLiveData<Int>()
    var hoursPerDay: Int

    init {
        val dao = PowerCostDatabase.getInstance(application).dao

        repository = PowerCostRepository(dao = dao)

        watts.value = 0

        hoursPerDay = 1
    }


    fun readPowerCost(isoCode: String): MutableLiveData<PowerCost> {
        val data = MutableLiveData<PowerCost>()

        viewModelScope.launch(Dispatchers.IO) {
            data.postValue((repository.readPowerCost(isoCode.capitalize())))
        }
        return data
    }

    fun countBaseHourCost(watts: Int, cost: Double ): Double{

        return PowerCostCalculator.countHourCost(watts, cost)
    }

}