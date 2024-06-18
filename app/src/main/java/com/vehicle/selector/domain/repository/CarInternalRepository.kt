package com.vehicle.selector.domain.repository

import com.vehicle.selector.domain.entity.CarEntity

interface CarInternalRepository {

    suspend fun saveCarEntity(carEntity: CarEntity)

    suspend fun updateName(carEntity: CarEntity)

    suspend fun deleteCarEntity(carEntityId: Int): Int

    suspend fun getCarEntityById(carEntityId: Int): CarEntity

    suspend fun getAllCarEntity(): List<CarEntity>
}