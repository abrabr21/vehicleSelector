package com.vehicle.selector.domain.mapper

import com.vehicle.selector.data.db.table.VehicleInternalDto
import com.vehicle.selector.domain.entity.CarEntity

fun VehicleInternalDto.toCarEntity() : CarEntity =
    CarEntity(
        id = this.id.toInt(),
        name = this.name,
        brand = this.brand,
        brandKey = this.brandKey,
        model = this.model,
        year = this.year
    )

fun CarEntity.toVehicleInternalDto() : VehicleInternalDto =
    VehicleInternalDto(
        name = this.name,
        brand = this.brand,
        brandKey = this.brandKey,
        model = this.model,
        year = this.year
    )