package com.vehicle.selector.presentation.selection_flow.epoxy

import com.airbnb.epoxy.TypedEpoxyController
import com.vehicle.selector.domain.entity.CarEntity

class CarSelectorController(
    private val onItemClick: (CarEntity) -> Unit = {},
    private val onRemoveClick: (CarEntity) -> Unit = {}
) : TypedEpoxyController<List<CarEntity>>() {

    override fun buildModels(data: List<CarEntity>) {
        data.forEach {
                epoxyCar {
                    id(it.id)
                    carEntity(it)
                    onItemClick(onItemClick)
                    onRemoveClick(onRemoveClick)
                }
            }
    }
}