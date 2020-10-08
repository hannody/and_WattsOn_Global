package com.abunayla.wattson.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

private const val tableName = "power_cost_table"
@Entity(tableName = tableName, indices = [Index(value =["iso_code","cost", "currency"])])
data class PowerCost (
    @NonNull
    var cost: Double,
    @PrimaryKey
    var iso_code: String,
    @NonNull
    var currency: String,
)
