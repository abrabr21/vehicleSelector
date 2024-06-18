package com.vehicle.selector.domain.entity

data class BrandDetailEntity(
    val page: Int,
    val pageSize: Int,
    val totalPageCount: Int,
    val brandEntityList: List<BrandEntity>
): java.io.Serializable
