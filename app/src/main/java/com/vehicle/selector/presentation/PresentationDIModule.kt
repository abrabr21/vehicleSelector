package com.vehicle.selector.presentation

import com.vehicle.selector.presentation.configuration_flow.MainCarConfigurationViewModel
import com.vehicle.selector.presentation.selection_flow.MainVehicleSelectorViewModel
import com.vehicle.selector.presentation.selection_flow.details.VehicleDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        MainVehicleSelectorViewModel(
            savedStateHandle = get(),
            getCarEntityListUseCase = get(),
            deleteCarEntityUseCase = get()
        )
    }
    viewModel {
        MainCarConfigurationViewModel(
            savedStateHandle = get(),
            getBrandDetailsUseCase = get(),
            getModelDetailsUseCase = get(),
            getYearDetailsUseCase = get(),
            insertCarEntityUseCase = get()
        )
    }
    viewModel {
        VehicleDetailsViewModel(
            savedStateHandle = get(),
            getCarEntityByIdUseCase = get(),
            updateCarEntityUseCase = get()
        )
    }
}