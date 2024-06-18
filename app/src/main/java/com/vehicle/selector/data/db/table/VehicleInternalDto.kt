package com.vehicle.selector.data.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_entity")
data class VehicleInternalDto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val brand: String,
    val brandKey: String,
    val model: String,
    val year: String
)