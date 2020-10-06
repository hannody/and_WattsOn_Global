package com.abunayla.wattson.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PowerCostDao {
    @Query("SELECT * FROM `power_cost_july_2020.sql` WHERE isoCode = :isoCode LIMIT 1" )
    suspend fun getPowerCost(isoCode: String): List<PowerCost>
}