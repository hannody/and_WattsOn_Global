package com.abunayla.wattson.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PowerCostDao {
    @Query("SELECT * FROM power_cost_table WHERE iso_code = :isoCode LIMIT 1" )
    suspend fun readPowerCost(isoCode: String): PowerCost
}