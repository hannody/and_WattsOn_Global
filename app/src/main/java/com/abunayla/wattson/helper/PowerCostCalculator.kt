package com.abunayla.wattson.helper

class PowerCostCalculator {
    companion object {
        fun countHourCost(watts: Int = 1, kWh_cost: Double): Double {
            val wh: Double = watts / 1000.00
            return (wh * kWh_cost)
        }

        fun countDailyCost(hours_per_day: Int, one_hour_cost: Double): Double {
            return hours_per_day * one_hour_cost
        }

        fun countWeeklyCost(cost_per_day: Double): Double {
            return cost_per_day * 7
        }

        fun countMonthlyCost(cost_per_day: Double, days_per_month: Double = 30.4368499): Double {

            return cost_per_day * days_per_month
        }

        fun countYearlyCost(cost_per_day: Double, days_per_year: Double = 365.242199): Double {
            return cost_per_day * days_per_year
        }
    }
}