package com.abunayla.wattson.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abunayla.wattson.db.PowerCost
import com.abunayla.wattson.db.PowerCostDatabase
import com.abunayla.wattson.repository.PowerCostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class PowerCostViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PowerCostRepository

    init {
        val dao = PowerCostDatabase.getInstance(application).dao
        repository = PowerCostRepository(dao = dao)
    }


    fun readPowerCost(isoCode: String): MutableLiveData<PowerCost> {
        var data = MutableLiveData<PowerCost>()

        viewModelScope.launch(Dispatchers.IO) {
            data.postValue((repository.readPowerCost(isoCode.capitalize(Locale.ROOT))))
        }
        return data
    }
}