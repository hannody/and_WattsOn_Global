package com.abunayla.wattson.repository

import com.abunayla.wattson.db.PowerCost
import com.abunayla.wattson.db.PowerCostDao

class PowerCostRepository(private val dao:PowerCostDao) {
    suspend fun readPowerCost(isoCode: String): PowerCost{
        return dao.readPowerCost(isoCode)
    }
}