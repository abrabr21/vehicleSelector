package com.vehicle.selector

import com.vehicle.selector.data.db.VehicleDB
import com.vehicle.selector.data.db.table.VehicleInternalDto
import com.vehicle.selector.data.repository.CarInternalRepositoryImpl
import com.vehicle.selector.domain.entity.CarEntity
import com.vehicle.selector.domain.mapper.toCarEntity
import com.vehicle.selector.domain.mapper.toVehicleInternalDto
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class CarInternalRepositoryImplTest {
    private lateinit var vehicleDB: VehicleDB
    private lateinit var carInternalRepository: CarInternalRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        vehicleDB = mockk()
        carInternalRepository = CarInternalRepositoryImpl(vehicleDB)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `test saveCarEntity`() = runBlocking {
        val carEntity = CarEntity(0, "MYCar", "Volvo", "140", "XC60", "2022")

        coEvery { vehicleDB.vehicleDao().insert(any()) } returns Unit

        val result = carInternalRepository.saveCarEntity(carEntity)

        assertEquals(Unit, result)
    }

    @Test
    fun `test updateName`() = runBlocking {
        val carEntity = CarEntity(0, "MYCar", "Volvo", "140", "XC60", "2022")

        coEvery { vehicleDB.vehicleDao().updateName(any(), any()) } returns Unit

        val result = carInternalRepository.updateName(carEntity)

        assertEquals(Unit, result)
    }

    @Test
    fun `test deleteCarEntity`() = runBlocking {
        val carEntityId = 0

        coEvery { vehicleDB.vehicleDao().delete(0) } returns 0

        val result = carInternalRepository.deleteCarEntity(carEntityId)

        assertEquals(0, result)
    }

    @Test
    fun `test getCarEntityById`() = runBlocking {
        val carEntity = CarEntity(0, "MYCar", "Volvo", "140", "XC60", "2022")

        coEvery {
            vehicleDB.vehicleDao().getVehicleById(0)
        } returns carEntity.toVehicleInternalDto()

        val result = carInternalRepository.getCarEntityById(0)

        assertEquals(carEntity, result)
    }

    @Test
    fun `test getAllCarEntity`() = runBlocking {
        val vehicles = listOf(
            VehicleInternalDto(0, "MYCar", "Volvo", "140", "XC60", "2022"),
            VehicleInternalDto(1, "Bmw", "BMW", "130", "i8", "2022")
        )

        coEvery {
            vehicleDB.vehicleDao().getVehicles()
        } returns vehicles

        val result = carInternalRepository.getAllCarEntity()

        assertEquals(vehicles.map { it.toCarEntity() }, result)
    }
}