package com.vehicle.selector.data.repository

import com.vehicle.selector.data.dto.GeneralManufacturedDto
import com.vehicle.selector.data.dto.YearManufacturedDto
import com.vehicle.selector.data.utills.resultOf
import com.vehicle.selector.domain.repository.CarRemoteRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

//put here your base url and api key for proper requests
const val END_POINT_GET_USER_KTOR = "Base-Url"
const val API_KEY = "api-key"
const val PAGE_ITEM_SIZE = 15

class CarRemoteRepositoryImpl(
    private val httpClient: HttpClient
) : CarRemoteRepository {
    override suspend fun getBrandDetails(pageNumber: Int): Result<GeneralManufacturedDto> {
        return resultOf {
            httpClient.get<GeneralManufacturedDto>("$END_POINT_GET_USER_KTOR/v1/car-types/manufacturer") {
                parameter("wa_key", API_KEY)
                parameter("page", pageNumber)
                parameter("pageSize", PAGE_ITEM_SIZE)
            }
        }
    }

    override suspend fun getModelDetails(
        brandKey: String,
        pageNumber: Int
    ): Result<GeneralManufacturedDto> {
        return resultOf {
            httpClient.get<GeneralManufacturedDto>("$END_POINT_GET_USER_KTOR/v1/car-types/main-types") {
                parameter("wa_key", API_KEY)
                parameter("manufacturer", brandKey)
                parameter("page", pageNumber)
                parameter("pageSize", PAGE_ITEM_SIZE)
            }
        }
    }

    override suspend fun getYearDetails(
        brandKey: String,
        modelKey: String
    ): Result<YearManufacturedDto> {
        return resultOf {
            httpClient
                .get<YearManufacturedDto>("$END_POINT_GET_USER_KTOR/v1/car-types/built-dates") {
                    parameter("wa_key", API_KEY)
                    parameter("manufacturer", brandKey)
                    parameter("main-type", modelKey)
                }
        }
    }
}