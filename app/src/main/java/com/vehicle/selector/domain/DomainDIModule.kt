package com.vehicle.selector.domain

import com.vehicle.selector.domain.usecase.DeleteCarEntityUseCase
import com.vehicle.selector.domain.usecase.GetBrandDetailsUseCase
import com.vehicle.selector.domain.usecase.GetCarEntityByIdUseCase
import com.vehicle.selector.domain.usecase.GetCarEntityListUseCase
import com.vehicle.selector.domain.usecase.GetModelDetailsUseCase
import com.vehicle.selector.domain.usecase.GetYearDetailsUseCase
import com.vehicle.selector.domain.usecase.InsertCarEntityUseCase
import com.vehicle.selector.domain.usecase.UpdateCarEntityUseCase
import org.koin.dsl.module

val remoteModule = module {
    factory {
        GetBrandDetailsUseCase(carRemoteRepository = get())
    }
    factory { GetModelDetailsUseCase(repository = get()) }
    factory { GetYearDetailsUseCase(repository = get()) }
}

val internalModule = module {
    factory { GetCarEntityListUseCase(carInternalRepository = get()) }
    factory { InsertCarEntityUseCase(carInternalRepository = get()) }
    factory { DeleteCarEntityUseCase(carInternalRepository = get()) }
    factory { GetCarEntityByIdUseCase(carInternalRepository = get()) }
    factory { UpdateCarEntityUseCase(carInternalRepository = get()) }
}


val domainModule = module { includes(remoteModule, internalModule) }