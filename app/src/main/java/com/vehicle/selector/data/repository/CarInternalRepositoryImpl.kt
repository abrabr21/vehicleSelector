package com.vehicle.selector.data.repository

import com.vehicle.selector.data.db.VehicleDB
import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.domain.mapper.toCarEntity
import com.vehicle.selector.domain.mapper.toVehicleInternalDto
import com.vehicle.selector.domain.repository.CarInternalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarInternalRepositoryImpl(private val vehicleDB: VehicleDB) : CarInternalRepository {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun saveCarEntity(carEntity: CarEntity) = withContext(ioDispatcher) {
        vehicleDB.vehicleDao().insert(carEntity.toVehicleInternalDto())
    }

    override suspend fun updateName(carEntity: CarEntity) = withContext(ioDispatcher) {
        vehicleDB.vehicleDao().updateName(carEntity.id, carEntity.name)
    }

    override suspend fun deleteCarEntity(carEntityId: Int): Int = withContext(ioDispatcher) {
        vehicleDB.vehicleDao().delete(carEntityId)
    }

    override suspend fun getCarEntityById(carEntityId: Int): CarEntity = withContext(ioDispatcher) {
        vehicleDB.vehicleDao().getVehicleById(carEntityId).toCarEntity()
    }

    override suspend fun getAllCarEntity(): List<CarEntity> = withContext(ioDispatcher) {
        vehicleDB.vehicleDao().getVehicles().map { it.toCarEntity() }
    }
}