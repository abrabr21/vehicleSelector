package com.vehicle.selector.domain.usecase

import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.domain.repository.CarInternalRepository

class GetCarEntityByIdUseCase(private val carInternalRepository: CarInternalRepository) {

    suspend operator fun invoke(params: Int): Result<CarEntity> {
        val result = carInternalRepository.getCarEntityById(params)
        return Result.success(result)
    }
}