package com.vehicle.selector.domain.mapper

import com.vehicle.selector.data.dto.GeneralManufacturedDto
import com.vehicle.selector.data.dto.YearManufacturedDto
import com.vehicle.selector.domain.entity.BrandDetailEntity
import com.vehicle.selector.domain.entity.BrandEntity
import com.vehicle.selector.domain.entity.ModelDetailEntity
import com.vehicle.selector.domain.entity.ModelEntity
import com.vehicle.selector.domain.entity.YearDetailEntity
import com.vehicle.selector.domain.entity.YearEntity

fun GeneralManufacturedDto.toBrandDetailEntity(): BrandDetailEntity {
    val brandEntityList = this.key.map { BrandEntity(brandKey = it.key, name = it.value) }
    return BrandDetailEntity(
        page = page,
        pageSize = pageSize,
        totalPageCount = totalPageCount,
        brandEntityList = brandEntityList
    )
}

fun GeneralManufacturedDto.toModelDetailEntity(): ModelDetailEntity {
    val modelEntityList = this.key.map { ModelEntity(modelKey = it.key, name = it.value) }
    return ModelDetailEntity(
        page = page,
        pageSize = pageSize,
        totalPageCount = totalPageCount,
        modelEntityList = modelEntityList
    )
}

fun YearManufacturedDto.toYearDetailEntity(): YearDetailEntity {
    val yearEntityList = this.key.map { YearEntity(year = it.value) }
    return YearDetailEntity(
        yearEntityList = yearEntityList
    )
}

const val PAGE_SCALE_FACTOR = 1