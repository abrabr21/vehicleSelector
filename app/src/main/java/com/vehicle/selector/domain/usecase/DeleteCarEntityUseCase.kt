package com.vehicle.selector.domain.usecase

import com.vehicle.selector.domain.repository.CarInternalRepository

class DeleteCarEntityUseCase(private val carInternalRepository: CarInternalRepository) {
    suspend operator fun  invoke(carEntityId: Int): Result<Int> {
        val result = carInternalRepository.deleteCarEntity(carEntityId)
        return Result.success(result)
    }
}