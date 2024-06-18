package com.vehicle.selector.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeneralManufacturedDto(
    val page: Int,
    val pageSize: Int,
    val totalPageCount: Int,
    val key: Map<String, String>
)

@Serializable
data class YearManufacturedDto(
    val key: Map<String, String>
)