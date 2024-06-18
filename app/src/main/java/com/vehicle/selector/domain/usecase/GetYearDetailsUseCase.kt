package com.vehicle.selector.domain.usecase

import com.vehicle.selector.domain.entity.YearDetailEntity
import com.vehicle.selector.domain.mapper.toYearDetailEntity
import com.vehicle.selector.domain.repository.CarRemoteRepository

class GetYearDetailsUseCase(private val repository: CarRemoteRepository) {

    suspend operator fun invoke(brandKey: String, modelKey: String): Result<YearDetailEntity> {
        val result = repository.getYearDetails(brandKey, modelKey).getOrNull()?.toYearDetailEntity()
            ?: return Result.failure(NullPointerException("Couldn`t get Year List "))
        return Result.success(result)
    }
}