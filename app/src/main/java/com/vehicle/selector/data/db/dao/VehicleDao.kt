package com.vehicle.selector.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vehicle.selector.data.db.table.VehicleInternalDto

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vehicle: VehicleInternalDto)


    @Query("UPDATE car_entity SET name = :vehicleName WHERE id = :vehicleId")
    fun updateName(vehicleId: Int, vehicleName: String)

    @Query("DELETE FROM car_entity WHERE id = :vehicleId")
    fun delete(vehicleId: Int) : Int

    @Query("SELECT * FROM car_entity WHERE id = :vehicleId")
    fun getVehicleById(vehicleId: Int) : VehicleInternalDto

    @Query("SELECT * FROM car_entity")
    fun getVehicles(): List<VehicleInternalDto>
}