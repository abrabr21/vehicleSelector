package com.vehicle.selector.presentation.selection_flow.details

import com.vehicle.selector.domain.entity.CarEntity

data class VehicleDetailsState(
    val carEntity: CarEntity = CarEntity.getEmptyCarEntity(),
    val isLoading: Boolean = true
) : java.io.Serializable

sealed class VehicleDetailsEvent {
    data class LoadDetailsInfo(val id: Int) : VehicleDetailsEvent()
    data class OnNameUpdated(val name: String) : VehicleDetailsEvent()
    data object Save : VehicleDetailsEvent()
}

sealed class VehicleDetailsSingleAction {
    data object Close : VehicleDetailsSingleAction()
    data object ShowUnknownError : VehicleDetailsSingleAction()
}