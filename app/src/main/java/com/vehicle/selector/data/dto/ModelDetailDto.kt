package com.vehicle.selector.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ModelDetailDto(
    val page: Int,
    val pageSize: Int,
    val totalPageCount: Int,
    val key: Map<String, String>
)