package com.vehicle.selector.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vehicle.selector.data.db.dao.VehicleDao
import com.vehicle.selector.data.db.table.VehicleInternalDto

@Database(entities = [VehicleInternalDto::class], version = 1)
abstract class VehicleDB : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao

    companion object {
        @Volatile
        private var INSTANCE: VehicleDB? = null

        fun getInstance(context: Context): VehicleDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                VehicleDB::class.java, "app_database.db"
            ).build()
    }
}