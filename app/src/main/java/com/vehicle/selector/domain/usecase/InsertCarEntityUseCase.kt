package com.vehicle.selector.domain.usecase

import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.domain.repository.CarInternalRepository

class InsertCarEntityUseCase(private val carInternalRepository: CarInternalRepository) {

    suspend operator fun invoke(carEntity: CarEntity): Result<Unit> {
        carInternalRepository.saveCarEntity(carEntity)
        return Result.success(Unit)
    }
}