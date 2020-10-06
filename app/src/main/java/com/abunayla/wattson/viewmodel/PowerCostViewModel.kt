package com.abunayla.wattson.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abunayla.wattson.db.PowerCostDatabase
import com.abunayla.wattson.repository.PowerCostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PowerCostViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PowerCostRepository

    init {
        val dao = PowerCostDatabase.getInstance(application).dao
        repository = PowerCostRepository(dao = dao)
    }

    fun readPowerCost(isoCode: String): LiveData<Double>{
        val cost = MutableLiveData<Double>()

        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.readPowerCost(isoCode)
            //cost.value = data.first().cost
        }
        return cost
    }
}