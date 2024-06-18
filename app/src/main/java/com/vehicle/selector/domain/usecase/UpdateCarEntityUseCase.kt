package com.vehicle.selector.domain.usecase

import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.domain.repository.CarInternalRepository

class UpdateCarEntityUseCase(private val carInternalRepository: CarInternalRepository) {

    suspend operator fun invoke(carEntity: CarEntity): Result<Unit> {
        carInternalRepository.updateName(carEntity)
        return Result.success(Unit)
    }
}