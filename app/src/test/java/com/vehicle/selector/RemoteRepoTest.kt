package com.vehicle.selector

import com.vehicle.selector.data.dto.GeneralManufacturedDto
import com.vehicle.selector.data.dto.YearManufacturedDto
import com.vehicle.selector.data.repository.CarRemoteRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RemoteRepoTest {

    private val apiMockEngine = ApiMockEngine()
    private val engine = apiMockEngine.get()
    private val client = HttpClient(engine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
    private val repo = CarRemoteRepositoryImpl(client)

    @Test
    fun `test brands`() = runBlocking {
        val expectedResult = GeneralManufacturedDto(
            page = 0,
            pageSize = 15,
            totalPageCount = 6,
            key = mapOf(
                "107" to "Bentley",
                "125" to "Borgward",
                "130" to "BMW",
                "141" to "Buick",
                "145" to "Brilliance",
                "150" to "Cadillac",
                "157" to "Caterham",
                "160" to "Chevrolet",
                "020" to "Abarth",
                "040" to "Alfa Romeo",
                "042" to "Alpina",
                "043" to "Alpine",
                "057" to "Aston Martin",
                "060" to "Audi",
                "095" to "Barkas"
            )
        )
        val response = repo.getBrandDetails(0).getOrThrow()
        assertEquals(expectedResult, response)
    }

    @Test
    fun `test model`() = runBlocking {
        val expectedResult = GeneralManufacturedDto(
            page = 0,
            pageSize = 15,
            totalPageCount = 6,
            key = mapOf(
                "1er" to "1er",
                "2er" to "2er",
                "3er" to "3er",
                "4er" to "4er",
                "5er" to "5er",
                "6er" to "6er",
                "7er" to "7er",
                "8er" to "8er",
                "i3" to "i3",
                "i8" to "i8",
                "X1" to "X1",
                "X2" to "X2",
                "X3" to "X3",
                "X4" to "X4",
                "X5" to "X5",
                "X6" to "X6",
                "X7" to "X7",
                "Z1" to "Z1",
                "Z3" to "Z3",
                "Z4" to "Z4",
                "Z8" to "Z8"
            )
        )
        val response = repo.getModelDetails("130",0).getOrThrow()
        assertEquals(expectedResult, response)
    }

    @Test
    fun `test year`() = runBlocking {
                val expectedResult = YearManufacturedDto(
            mapOf(
                "2014" to "2014",
                "2015" to "2015",
                "2016" to "2016",
                "2017" to "2017",
                "2018" to "2018",
                "2019" to "2019"
            )
        )
        val response = repo.getYearDetails("130","i8").getOrThrow()
        assertEquals(expectedResult, response)
    }
}

class ApiMockEngine {
    fun get() = client.engine

    private val responseHeaders =
        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
    private val client = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                when (request.url.encodedPath) {
                    "/v1/car-types/manufacturer" ->
                        respond(GeneralBrandDtoResponse(), HttpStatusCode.OK, responseHeaders)

                    "/v1/car-types/main-types" ->
                        respond(GeneralModelDtoResponse(), HttpStatusCode.OK, responseHeaders)

                    "/v1/car-types/built-dates" ->
                        respond(GeneralYearDtoResponse(), HttpStatusCode.OK, responseHeaders)

                    else -> error("Unhandled ${request.url.encodedPath}")
                }
            }
        }
    }
}

object GeneralModelDtoResponse {
    operator fun invoke(): String = "{\n" +
            "  \"page\": 0,\n" +
            "  \"pageSize\": 15,\n" +
            "  \"totalPageCount\": 6,\n" +
            "  \"key\": {\n" +
            "    \"1er\": \"1er\",\n" +
            "    \"2er\": \"2er\",\n" +
            "    \"3er\": \"3er\",\n" +
            "    \"4er\": \"4er\",\n" +
            "    \"5er\": \"5er\",\n" +
            "    \"6er\": \"6er\",\n" +
            "    \"7er\": \"7er\",\n" +
            "    \"8er\": \"8er\",\n" +
            "    \"i3\": \"i3\",\n" +
            "    \"i8\": \"i8\",\n" +
            "    \"X1\": \"X1\",\n" +
            "    \"X2\": \"X2\",\n" +
            "    \"X3\": \"X3\",\n" +
            "    \"X4\": \"X4\",\n" +
            "    \"X5\": \"X5\",\n" +
            "    \"X6\": \"X6\",\n" +
            "    \"X7\": \"X7\",\n" +
            "    \"Z1\": \"Z1\",\n" +
            "    \"Z3\": \"Z3\",\n" +
            "    \"Z4\": \"Z4\",\n" +
            "    \"Z8\": \"Z8\"\n" +
            "  }\n" +
            "}"
}

object GeneralBrandDtoResponse {
    operator fun invoke(): String = "{\n" +
            "  \"page\": 0,\n" +
            "  \"pageSize\": 15,\n" +
            "  \"totalPageCount\": 6,\n" +
            "  \"key\": {\n" +
            "    \"107\": \"Bentley\",\n" +
            "    \"125\": \"Borgward\",\n" +
            "    \"130\": \"BMW\",\n" +
            "    \"141\": \"Buick\",\n" +
            "    \"145\": \"Brilliance\",\n" +
            "    \"150\": \"Cadillac\",\n" +
            "    \"157\": \"Caterham\",\n" +
            "    \"160\": \"Chevrolet\",\n" +
            "    \"020\": \"Abarth\",\n" +
            "    \"040\": \"Alfa Romeo\",\n" +
            "    \"042\": \"Alpina\",\n" +
            "    \"043\": \"Alpine\",\n" +
            "    \"057\": \"Aston Martin\",\n" +
            "    \"060\": \"Audi\",\n" +
            "    \"095\": \"Barkas\"\n" +
            "  }\n" +
            "}"
}

object GeneralYearDtoResponse {
    operator fun invoke(): String = "{\n" +
            "  \"key\": {\n" +
            "    \"2014\": \"2014\",\n" +
            "    \"2015\": \"2015\",\n" +
            "    \"2016\": \"2016\",\n" +
            "    \"2017\": \"2017\",\n" +
            "    \"2018\": \"2018\",\n" +
            "    \"2019\": \"2019\"\n" +
            "  }\n" +
            "}"
}

