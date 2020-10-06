package com.abunayla.wattson.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "power_cost_july_2020.sql")
data class PowerCost (
    var cost: Double,
    @PrimaryKey
    var isoCode: String,
    var currency: String,
)
