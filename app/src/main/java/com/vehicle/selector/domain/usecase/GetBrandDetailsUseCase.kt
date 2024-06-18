package com.vehicle.selector.domain.usecase

import com.vehicle.selector.domain.entity.BrandDetailEntity
import com.vehicle.selector.domain.mapper.toBrandDetailEntity
import com.vehicle.selector.domain.repository.CarRemoteRepository

class GetBrandDetailsUseCase(private val carRemoteRepository: CarRemoteRepository) {
    suspend operator fun invoke(params: Int): Result<BrandDetailEntity> {
        val response =
            carRemoteRepository.getBrandDetails(params).getOrNull()?.toBrandDetailEntity()
                ?: return Result.failure(NullPointerException("Couldn`t get Brand List "))
        return Result.success(response)
    }
}