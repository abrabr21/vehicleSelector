package com.vehicle.selector.domain.entity

data class CarEntity(
    val id: Int,
    val name: String,
    val brand: String,
    val brandKey: String,
    val model: String,
    val year: String
): java.io.Serializable {
    companion object {
        const val ID_UNKNOWN = -1

        fun getEmptyCarEntity() : CarEntity = CarEntity(
            id = ID_UNKNOWN,
            name = "",
            brand = "",
            brandKey = "",
            model = "",
            year = "",
        )
    }
}
