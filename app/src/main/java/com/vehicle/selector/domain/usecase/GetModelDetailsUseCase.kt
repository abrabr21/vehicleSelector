package com.vehicle.selector.domain.usecase

import com.vehicle.selector.domain.entity.ModelDetailEntity
import com.vehicle.selector.domain.mapper.toModelDetailEntity
import com.vehicle.selector.domain.repository.CarRemoteRepository

class GetModelDetailsUseCase(private val repository: CarRemoteRepository) {
    suspend operator fun invoke(params: Pair<Int, String>): Result<ModelDetailEntity> {
        val result = repository.getModelDetails(params.second, params.first).getOrNull()
            ?.toModelDetailEntity()
            ?: return Result.failure(NullPointerException("Couldn`t get Model List "))
        return Result.success(result)
    }
}