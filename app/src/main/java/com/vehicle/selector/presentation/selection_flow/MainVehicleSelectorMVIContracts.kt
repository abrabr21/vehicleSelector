package com.vehicle.selector.presentation.selection_flow

import com.vehicle.selector.domain.entity.CarEntity

data class MainVehicleSelectorState(
    val carList : List<CarEntity>,
    val isLoading: Boolean
) : java.io.Serializable

sealed class MainVehicleSelectorEvents {
    data class OnItemRemoveClick(val carEntity: CarEntity) : MainVehicleSelectorEvents()
    data object LoadData : MainVehicleSelectorEvents()
}

sealed class MainVehicleSelectorSingleAction{
    data object OnShowCommonError : MainVehicleSelectorSingleAction()
}
