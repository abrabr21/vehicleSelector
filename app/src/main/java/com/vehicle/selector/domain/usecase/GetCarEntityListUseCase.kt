package com.vehicle.selector.domain.usecase

import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.domain.repository.CarInternalRepository

class GetCarEntityListUseCase(private val carInternalRepository: CarInternalRepository) {
    suspend operator fun invoke(params: Unit): Result<List<CarEntity>> {
        val result = carInternalRepository.getAllCarEntity()
        return Result.success(result)
    }
}