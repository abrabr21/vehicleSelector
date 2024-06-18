package com.vehicle.selector.data

import com.vehicle.selector.data.db.VehicleDB
import com.vehicle.selector.data.repository.CarInternalRepositoryImpl
import com.vehicle.selector.data.repository.CarRemoteRepositoryImpl
import com.vehicle.selector.domain.repository.CarInternalRepository
import com.vehicle.selector.domain.repository.CarRemoteRepository
import org.koin.dsl.module

val dataModule = module {
    single { VehicleDB.getInstance(context = get()) }
    single<CarRemoteRepository> {
        CarRemoteRepositoryImpl(
            httpClient = httpClientAndroid
        )
    }
    single<CarInternalRepository> {
        CarInternalRepositoryImpl(
            vehicleDB = get()
        )
    }
}