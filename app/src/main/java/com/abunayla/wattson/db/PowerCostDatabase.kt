package com.abunayla.wattson.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PowerCost::class], version = 1, exportSchema = false)
abstract class PowerCostDatabase: RoomDatabase() {
    abstract val dao: PowerCostDao

    companion object {
        @Volatile
        private var INSTANCE: PowerCostDatabase? = null
        fun getInstance(context: Context): PowerCostDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        PowerCostDatabase::class.java, "db1.db")
                        .createFromAsset("database/db1.db")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}