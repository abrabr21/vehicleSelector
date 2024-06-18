package com.vehicle.selector.domain.entity

data class ModelDetailEntity (
    val page: Int,
    val pageSize: Int,
    val totalPageCount: Int,
    val modelEntityList: List<ModelEntity>
): java.io.Serializable