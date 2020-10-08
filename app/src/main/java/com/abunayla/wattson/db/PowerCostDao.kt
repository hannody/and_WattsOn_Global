package com.abunayla.wattson.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PowerCostDao {
    @Query("SELECT * FROM test_table WHERE iso_code = :isoCode LIMIT 1" )
    suspend fun getPowerCost(isoCode: String): List<PowerCost>
}