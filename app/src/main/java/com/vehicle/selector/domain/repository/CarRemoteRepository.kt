package com.vehicle.selector.domain.repository

import androidx.annotation.CheckResult
import com.vehicle.selector.data.dto.GeneralManufacturedDto
import com.vehicle.selector.data.dto.YearManufacturedDto

interface CarRemoteRepository {

    @CheckResult
    suspend fun getBrandDetails(pageNumber: Int): Result<GeneralManufacturedDto>

    @CheckResult
    suspend fun getModelDetails(brandKey: String, pageNumber: Int): Result<GeneralManufacturedDto>

    @CheckResult
    suspend fun getYearDetails(brandKey: String, modelKey: String): Result<YearManufacturedDto>
}